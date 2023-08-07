package com.example.social_media_api.controller;

import com.example.social_media_api.dto.reponse.ApiResponse;
import com.example.social_media_api.dto.reponse.CommentResponse;
import com.example.social_media_api.dto.request.CommentRequest;
import com.example.social_media_api.service.CommentService;
import com.example.social_media_api.utils.CommentCriteriaSearch;
import com.example.social_media_api.utils.CommentPage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/social-media-api/posts/{postId}/comments")
@Tag(name = "CRUD REST APIs for Comment Resource")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Create Comment REST API",
            description = "Create Comment REST API is used to create a new comment for a post"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED"
    )
    @SecurityRequirement(name = "Bear Authentication")
    public ResponseEntity<ApiResponse<String>> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest request
    ) {
        ApiResponse<String> apiResponse = new ApiResponse<>(commentService.createComment(postId, request));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Comment REST API",
            description = "Get Comment REST API is used to get a particular comment by its ID"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CommentResponse>> getCommentByIds(@PathVariable Long id) {
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>(commentService.getCommentById(id));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/getAllCommentPostsPaginationSortingSearch")
    @Operation(
            summary = "Get All Comments By Pagination Sorting Searching REST API",
            description = "Get All Comments REST API is used to get all comments for a specific post"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getAllCommentsByPaginationSortingSearching(
            @PathVariable Long postId,
            CommentPage commentPage,
            CommentCriteriaSearch commentSearchCriteria
    ) {
        List<CommentResponse> commentList = commentService.getAllCommentsByPostId(postId, commentPage, commentSearchCriteria);
        ApiResponse<List<CommentResponse>> apiResponse = new ApiResponse<>(commentList);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/getPostComment")
    @Operation(
            summary = "Get All Comments for a Post REST API",
            description = "Get  Comments REST API is used to get all comments for a specific post"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getAllCommentsForPost(
            @PathVariable Long postId
    ) {
        List<CommentResponse> commentList = commentService.getCommentInPost(postId);
        ApiResponse<List<CommentResponse>> apiResponse = new ApiResponse<>(commentList);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "Update Comment REST API",
            description = "Update Comment REST API is used to update an existing comment"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> updateComments(
            @PathVariable Long id,
            @RequestBody CommentRequest request
    ) {
        ApiResponse<String> apiResponse = new ApiResponse<>(commentService.updateComment(id, request));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{commentId}")
    @Operation(
            summary = "Delete Comment REST API",
            description = "Delete Comment REST API is used to delete a comment for a post"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        ApiResponse<String> apiResponse = new ApiResponse<>(commentService.deleteComment(postId, commentId));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}

