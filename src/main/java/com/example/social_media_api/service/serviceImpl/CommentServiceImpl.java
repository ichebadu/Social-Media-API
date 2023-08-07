package com.example.social_media_api.service.serviceImpl;

import com.example.social_media_api.dto.reponse.CommentResponse;
import com.example.social_media_api.dto.request.CommentRequest;
import com.example.social_media_api.entity.Comment;
import com.example.social_media_api.entity.Post;
import com.example.social_media_api.entity.User;
import com.example.social_media_api.exception.CommentNotFoundException;
import com.example.social_media_api.exception.PostNotFoundException;
import com.example.social_media_api.exception.UserNotFoundException;
import com.example.social_media_api.notificationEvent.CommentNotification;
import com.example.social_media_api.repository.CommentCriteriaRepository;
import com.example.social_media_api.repository.CommentRepository;
import com.example.social_media_api.repository.PostRepository;
import com.example.social_media_api.repository.UserRepository;
import com.example.social_media_api.service.CommentService;
import com.example.social_media_api.utils.CommentCriteriaSearch;
import com.example.social_media_api.utils.CommentPage;
import com.example.social_media_api.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
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
    private final ApplicationEventPublisher publisher;
    private final ModelMapper modelMapper;

    private final CommentCriteriaRepository commentCriteriaRepository;

    @Override
    public String createComment( Long postId, CommentRequest request) {
        User user = userRepository.findByEmail(UserUtils.getUserEmailFromContext())
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

        return user.getUsername()+ " Comment created successfully";
    }
    private void notifyPostCommented(User postOwner, User commenter, Post post, String comment) {
        String subject = "Post Comment Notification";
        String message = String.format("User %s commented on your post with ID: %d. Comment: %s",
                commenter.getUsername(), post.getId(), comment);
        publisher.publishEvent(new CommentNotification(postOwner, message, subject, commenter.getUsername()));
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
    public List<CommentResponse> getCommentInPost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new PostNotFoundException(""));
        return post.getComments()
                .stream().map((c)-> modelMapper.map(c,CommentResponse.class)).collect(Collectors.toList());
    }

    @Override
    public String updateComment(Long id, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        comment.setContent(commentRequest.getContent());
        commentRepository.save(comment);

        return "Comment updated successfully";
    }

    @Override
    public String deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new IllegalArgumentException("Comment does not belong to the specified post");
        }

        commentRepository.delete(comment);

        return "Comment deleted successfully";
    }
}
