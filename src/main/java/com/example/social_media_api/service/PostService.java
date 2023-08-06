package com.example.social_media_api.service;

import com.example.social_media_api.dto.reponse.PostResponse;
import com.example.social_media_api.dto.reponse.PostResponseContent;
import com.example.social_media_api.dto.request.PostRequest;
import com.example.social_media_api.utils.PostCriteriaSearch;
import com.example.social_media_api.utils.PostPage;

import java.util.List;

public interface PostService {

    PostResponse createPost(PostRequest postRequest);

    PostResponse likeOrUnlike(Long postId, boolean like);

    PostResponseContent getPostById(Long id);


    List<PostResponseContent> getAllPosts(PostPage postPage, PostCriteriaSearch postSearchCriteria);

    PostResponse updatePost(Long id, PostRequest postRequest);
    PostResponse deletePost(Long id);
}
