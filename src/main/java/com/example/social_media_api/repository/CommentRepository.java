package com.example.social_media_api.repository;

import com.example.social_media_api.dto.reponse.CommentResponse;
import com.example.social_media_api.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost_Id(Long postId);
}
