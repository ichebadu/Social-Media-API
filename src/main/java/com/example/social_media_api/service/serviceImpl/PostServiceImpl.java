package com.example.social_media_api.service.serviceImpl;

import com.example.social_media_api.dto.reponse.PostResponse;
import com.example.social_media_api.dto.request.PostRequest;
import com.example.social_media_api.entity.Post;
import com.example.social_media_api.entity.User;
import com.example.social_media_api.exception.PostNotFoundException;
import com.example.social_media_api.exception.UserNotFoundException;
import com.example.social_media_api.repository.PostRepository;
import com.example.social_media_api.repository.UserRepository;
import com.example.social_media_api.service.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public PostResponse createPost(PostRequest postRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = new Post();
        post.setContent(postRequest.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setLikesCount(0);
        post.setUser(user);
        postRepository.save(post);

        return PostResponse.builder()
                .message("Post created successfully")
                .build();
    }

    @Override
    public PostResponse incrementLikes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        int likesCount = post.getLikesCount();
        post.setLikesCount(likesCount + 1);

        postRepository.save(post);

        return modelMapper.map(post, PostResponse.class);
    }

    @Override
    public PostResponse decrementLikes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        int likesCount = post.getLikesCount();
        if (likesCount > 0) {
            post.setLikesCount(likesCount - 1);
        }

        postRepository.save(post);

        return modelMapper.map(post, PostResponse.class);
    }

    @Override
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));

        return modelMapper.map(post, PostResponse.class);
    }

    @Override
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> modelMapper.map(post, PostResponse.class))
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

