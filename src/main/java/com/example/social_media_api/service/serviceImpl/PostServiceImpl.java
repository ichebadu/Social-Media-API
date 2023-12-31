package com.example.social_media_api.service.serviceImpl;

import com.example.social_media_api.dto.reponse.PostResponse;
import com.example.social_media_api.dto.reponse.PostResponseContent;
import com.example.social_media_api.dto.request.PostRequest;
import com.example.social_media_api.entity.Post;
import com.example.social_media_api.entity.User;
import com.example.social_media_api.exception.PostNotFoundException;
import com.example.social_media_api.exception.UserNotFoundException;
import com.example.social_media_api.notificationEvent.PostEmailNotification;
import com.example.social_media_api.repository.PostCriteriaRepository;
import com.example.social_media_api.repository.PostRepository;
import com.example.social_media_api.repository.UserRepository;
import com.example.social_media_api.service.PostService;
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

    private final ApplicationEventPublisher publisher;
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
                .message(post.getUser() + ": your Post has been created successfully")
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
                .message(post.getUser() + "like created successfully")
                .build();
    }

    public void notifyPostLiked(User postOwner, User likes, Post post) {
        String subject = "Post Liked Notification";
        String message = String.format("USER %s liked your post with ID: %d", postOwner.getUsername(), post.getId());
        publisher.publishEvent(new PostEmailNotification(postOwner, subject, message, likes));
    }


    @Override
    public PostResponseContent getPostById(Long id) {
        userRepository.findByEmail(UserUtils.getUserEmailFromContext());
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));
        return modelMapper.map(post, PostResponseContent.class);
    }

    @Override
    public List<PostResponseContent> getAllPostsPaginateSortSearch(PostPage postPage, PostCriteriaSearch postSearchCriteria) {

        if (postPage.getPageSize() <= 0) {
            postPage.setPageSize(10);
        }
        if (postPage.getPageNumber() < 0) {
            postPage.setPageNumber(0);
        }

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
                .message(post.getId() + "Post updated successfully")
                .build();
    }

    @Override
    public PostResponse deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));
        postRepository.delete(post);
        return PostResponse.builder()
                .message(post.getTitle() + "deleted successfully")
                .build();
    }
    @Override
    public List<PostResponseContent> getAllPost(){
        User postUser = userRepository.findByEmail(UserUtils.getUserEmailFromContext())
                .orElseThrow(()-> new UserNotFoundException("USER NOT FOUND"));
       return postUser.getPosts()
               .stream()
               .map((c)-> modelMapper.map(c,PostResponseContent.class)).collect(Collectors.toList());
    }

}

