package com.example.social_media_api.service;

import com.example.social_media_api.dto.reponse.PostResponse;
import com.example.social_media_api.dto.reponse.PostResponseContent;
import com.example.social_media_api.dto.reponse.SearchResponsePage;
import com.example.social_media_api.dto.request.PostRequest;
import com.example.social_media_api.entity.Post;
import com.example.social_media_api.utils.PostCriteriaSearch;
import com.example.social_media_api.utils.PostPage;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    PostResponse createPost(PostRequest postRequest);
    List<PostResponseContent> getAllPost();

    PostResponse likeOrUnlike(Long postId, boolean like);

    PostResponseContent getPostById(Long id);

    Page<Post> getAllPostsPaginateSortSearch(PostPage postPage, PostCriteriaSearch postSearchCriteria);

    PostResponse updatePost(Long id, PostRequest postRequest);
    PostResponse deletePost(Long id);

}
