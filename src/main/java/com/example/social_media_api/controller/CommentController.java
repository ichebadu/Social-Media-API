package com.example.social_media_api.controller;

import com.example.social_media_api.dto.reponse.ApiResponse;
import com.example.social_media_api.dto.reponse.CommentResponse;
import com.example.social_media_api.dto.request.CommentRequest;
import com.example.social_media_api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/social-media-api/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(@PathVariable Long postId, @RequestBody CommentRequest request) {
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>(commentService.createComment(postId, request));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> getCommentById(@PathVariable Long id) {
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>(commentService.getCommentById(id));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getAllCommentsByPostId(@PathVariable Long postId) {
        ApiResponse<List<CommentResponse>> apiResponse = new ApiResponse<>(commentService.getAllCommentsByPostId(postId));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(@PathVariable Long id, @RequestBody CommentRequest request) {
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>(commentService.updateComment(id, request));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CommentResponse>> deleteComment(@PathVariable Long id) {
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>(commentService.deleteComment(id));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
