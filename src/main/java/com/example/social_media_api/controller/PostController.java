package com.example.social_media_api.controller;

import com.example.social_media_api.dto.reponse.ApiResponse;

import com.example.social_media_api.dto.reponse.PostResponse;
import com.example.social_media_api.dto.reponse.PostResponseContent;
import com.example.social_media_api.dto.request.PostRequest;
import com.example.social_media_api.service.PostService;
import com.example.social_media_api.utils.PostCriteriaSearch;
import com.example.social_media_api.utils.PostPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/social-media-api/posts")
@Tag(name = "CRUD REST APIs for Post Resource")
public class PostController {
    private final PostService postService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Create Post REST API",
            description = "Create Post REST API is used to save post into the database"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED"
    )
    @SecurityRequirement(name = "Bear Authentication")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@RequestBody PostRequest request) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>(postService.createPost(request));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Post REST API",
            description = "Get Post REST API is used to get a particular post from the database"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PostResponseContent>> getPostById(@PathVariable Long id) {
        ApiResponse<PostResponseContent> apiResponse = new ApiResponse<>(postService.getPostById(id));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            summary = "Get All Post REST API",
            description = "Get All Post REST API is used to get all posts from the database"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<PostResponseContent>>> getAllPosts(
            PostPage postPage,
            PostCriteriaSearch postSearchCriteria
    ) {
        List<PostResponseContent> postList = postService.getAllPosts(postPage, postSearchCriteria);
        ApiResponse<List<PostResponseContent>> apiResponse = new ApiResponse<>(postList);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update Post REST API",
            description = "Update Post REST API is used to update a post in the database"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    @SecurityRequirement(name = "Bear Authentication")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(@PathVariable Long id, @RequestBody PostRequest request) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>(postService.updatePost(id, request));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Post REST API",
            description = "Delete Post REST API is used to delete a particular post from the database"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("isAuthenticated() and #id == principal.post.id")
    public ResponseEntity<ApiResponse<PostResponse>> deletePost(@PathVariable Long id) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>(postService.deletePost(id));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/like")
    @Operation(
            summary = "Like/Unlike Post REST API",
            description = "Like/Unlike Post REST API is used to toggle the likes count of a post"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PostResponse>> likeOrUnlike(@PathVariable Long id, @RequestParam boolean like) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>(postService.likeOrUnlike(id, like));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}

