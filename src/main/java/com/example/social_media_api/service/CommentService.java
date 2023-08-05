package com.example.social_media_api.service;

import com.example.social_media_api.dto.reponse.CommentResponse;
import com.example.social_media_api.dto.request.CommentRequest;
import com.example.social_media_api.entity.Comment;
import com.example.social_media_api.utils.CommentCriteriaSearch;
import com.example.social_media_api.utils.CommentPage;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(Long postId, CommentRequest request);

    CommentResponse getCommentById(Long id);

    List<CommentResponse> getAllCommentsByPostId(Long postId, CommentPage commentPage, CommentCriteriaSearch commentSearchCriteria);

    CommentResponse updateComment(Long id, CommentRequest commentRequest);

    CommentResponse deleteComment(Long postId, Long commentId);
}
