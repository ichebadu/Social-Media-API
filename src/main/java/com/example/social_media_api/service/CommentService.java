package com.example.social_media_api.service;

import com.example.social_media_api.dto.reponse.CommentResponse;
import com.example.social_media_api.dto.request.CommentRequest;
import com.example.social_media_api.entity.Comment;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(Long postId, CommentRequest request);

    CommentResponse getCommentById(Long id);

    List<CommentResponse> getAllCommentsByPostId(Long postId);

    CommentResponse updateComment(Long id, CommentRequest commentRequest);

    CommentResponse deleteComment(Long id);
}
