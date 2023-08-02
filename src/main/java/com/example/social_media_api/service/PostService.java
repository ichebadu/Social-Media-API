package com.example.social_media_api.service;

import com.example.social_media_api.dto.reponse.PostResponse;
import com.example.social_media_api.dto.request.PostRequest;
import com.example.social_media_api.entity.Post;

import java.util.List;

public interface PostService {

    PostResponse createPost(PostRequest postRequest);

    PostResponse incrementLikes(Long postId);

    PostResponse decrementLikes(Long postId);

    PostResponse getPostById(Long id);
    List<PostResponse> getAllPosts();
    PostResponse updatePost(Long id, PostRequest postRequest);
    PostResponse deletePost(Long id);
}
