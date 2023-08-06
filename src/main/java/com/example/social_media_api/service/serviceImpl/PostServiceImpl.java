package com.example.social_media_api.service.serviceImpl;

import com.example.social_media_api.dto.reponse.PostResponse;
import com.example.social_media_api.dto.reponse.PostResponseContent;
import com.example.social_media_api.dto.request.PostRequest;
import com.example.social_media_api.entity.Post;
import com.example.social_media_api.entity.User;
import com.example.social_media_api.exception.PostNotFoundException;
import com.example.social_media_api.exception.UserNotFoundException;
import com.example.social_media_api.repository.PostCriteriaRepository;
import com.example.social_media_api.repository.PostRepository;
import com.example.social_media_api.repository.UserRepository;
import com.example.social_media_api.service.PostService;
import com.example.social_media_api.service.UserService;
import com.example.social_media_api.utils.PostCriteriaSearch;
import com.example.social_media_api.utils.PostPage;
import com.example.social_media_api.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //private final ApplicationEventPublisher publisher;
    private final ModelMapper modelMapper;
    private final PostCriteriaRepository postCriteriaRepository;

    public PostResponse createPost(PostRequest postRequest) {
        System.out.println(postRequest);

        User user = userRepository.findByEmail(UserUtils.getUserEmailFromContext())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post =  new Post();
        post.setCreatedAt(LocalDateTime.now());
               post.setTitle(postRequest.getTitle());
                       post.setContent(postRequest.getContent());
                       post.setLikesCount(0);
                               post.setUser(user);


        postRepository.save(post);

        return PostResponse.builder()
                .message("Post created successfully")
                .build();
    }

    @Override
    public PostResponse likeOrUnlike(Long postId, boolean like) {
        User user = userRepository.findByEmail(UserUtils.getUserEmailFromContext())
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        int likesCount = post.getLikesCount();
        System.out.println(user.getEmail());
        System.out.println(post.getTitle());
        if (like && likesCount < 1) {
            post.setLikesCount(likesCount + 1);
            post.getLikes().add(user);
            postRepository.save(post);
            notifyPostLiked(post.getUser(), user, post);
        } else {
            if (likesCount > 0) {
                post.setLikesCount(likesCount - 1);
                post.getLikes().remove(user);
                postRepository.save(post);
            }
        }


        return PostResponse.builder()
                .message("like created successfully")
                .build();
    }
    public void notifyPostLiked(User postOwner, User liker, Post post){
        String subject = "Post Linked Notification";
        String message = String.format("USER %s liked your post with ID: %d", liker.getUsername(), post.getId());
        //publisher.publishEvent(new PostNotificationService(postOwner.getEmail(),subject,message ));
    }

    @Override
    public PostResponseContent getPostById(Long id) {
        userRepository.findByEmail(UserUtils.getUserEmailFromContext());
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));
        return modelMapper.map(post, PostResponseContent.class);
    }


    @Override
    public List<PostResponseContent> getAllPosts(PostPage postPage, PostCriteriaSearch postSearchCriteria) {
        Page<Post> postPageResult = postCriteriaRepository.findAllWithFilter(postPage, postSearchCriteria);

        return postPageResult.getContent().stream()
                .map(post -> modelMapper.map(post, PostResponseContent.class))
                .collect(Collectors.toList());
    }


    @Override
    public PostResponse updatePost(Long id, PostRequest postRequest) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));

        post.setContent(postRequest.getContent());
        postRepository.save(post);


        return PostResponse.builder()
                .message("Post updated successfully")
                .build();
    }

    @Override
    public PostResponse deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));
        postRepository.delete(post);
        return PostResponse.builder()
                .message("deleted successfully")
                .build();
    }
}

