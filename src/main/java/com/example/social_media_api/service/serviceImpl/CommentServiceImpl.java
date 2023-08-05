package com.example.social_media_api.service.serviceImpl;

import com.example.social_media_api.dto.reponse.CommentResponse;
import com.example.social_media_api.dto.request.CommentRequest;
import com.example.social_media_api.entity.Comment;
import com.example.social_media_api.entity.Post;
import com.example.social_media_api.entity.User;
import com.example.social_media_api.exception.CommentNotFoundException;
import com.example.social_media_api.exception.PostNotFoundException;
import com.example.social_media_api.exception.UserNotFoundException;
import com.example.social_media_api.notificationEvent.PostLikesAndCommentNotification.PostNotificationService;
import com.example.social_media_api.repository.CommentCriteriaRepository;
import com.example.social_media_api.repository.CommentRepository;
import com.example.social_media_api.repository.PostRepository;
import com.example.social_media_api.repository.UserRepository;
import com.example.social_media_api.service.CommentService;
import com.example.social_media_api.utils.CommentCriteriaSearch;
import com.example.social_media_api.utils.CommentPage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final PostNotificationService postNotificationService;
    private final CommentCriteriaRepository commentCriteriaRepository;

    @Override
    public CommentResponse createComment( Long postId, CommentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(user);
        comment.setPost(post);
        commentRepository.save(comment);

        notifyPostCommented(post.getUser(), user, post, request.getContent());

        return CommentResponse.builder()
                .message("Comment created successfully")
                .username(request.getUsername())
                .build();
    }
    private void notifyPostCommented(User postOwner, User commenter, Post post, String comment) {
        String subject = "Post Comment Notification";
        String message = String.format("User %s commented on your post with ID: %d. Comment: %s",
                commenter.getUsername(), post.getId(), comment);
        postNotificationService.SendEmail(postOwner.getEmail(), subject, message);
    }

    @Override
    public CommentResponse getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        return modelMapper.map(comment, CommentResponse.class);
    }
    @Override
    public List<CommentResponse> getAllCommentsByPostId(Long postId, CommentPage commentPage, CommentCriteriaSearch commentSearchCriteria) {
        Page<Comment> commentPageResult = commentCriteriaRepository.findAllWithFilter(commentPage, commentSearchCriteria);

        return commentPageResult.getContent().stream()
                .map(comment -> modelMapper.map(comment, CommentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse updateComment(Long id, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        comment.setContent(commentRequest.getContent());
        commentRepository.save(comment);

        return CommentResponse.builder()
                .message("Comment updated successfully")
                .build();
    }

    @Override
    public CommentResponse deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        // Check if the comment belongs to the specified post
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new IllegalArgumentException("Comment does not belong to the specified post");
        }

        commentRepository.delete(comment);

        return CommentResponse.builder()
                .message("Comment deleted successfully")
                .build();
    }
}
