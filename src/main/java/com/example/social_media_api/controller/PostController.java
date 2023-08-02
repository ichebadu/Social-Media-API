package com.example.social_media_api.controller;

import com.example.social_media_api.dto.reponse.ApiResponse;
import com.example.social_media_api.dto.reponse.PostResponse;
import com.example.social_media_api.dto.request.PostRequest;
import com.example.social_media_api.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/social-media-api/posts")
public class PostController {
    private  final PostService postService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@RequestBody PostRequest request){
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>(postService.createPost(request));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Long id){
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>(postService.getPostById(id));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts(){
        ApiResponse<List<PostResponse>> apiResponse = new ApiResponse<>(postService.getAllPosts());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(@PathVariable Long id, @RequestBody PostRequest request){
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>(postService.updatePost(id,request));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() and #id == principal.post.id")
    public ResponseEntity<ApiResponse<PostResponse>> deletePost(@PathVariable Long id) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>(postService.deletePost(id));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PostResponse>> incrementLikes(@PathVariable Long id) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>(postService.incrementLikes(id));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/unlike")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PostResponse>> decrementLikes(@PathVariable Long id) {
        ApiResponse<PostResponse> apiResponse = new ApiResponse<>(postService.decrementLikes(id));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
