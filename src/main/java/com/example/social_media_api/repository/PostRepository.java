package com.example.social_media_api.repository;

import com.example.social_media_api.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

   /* @Query("SELECT p FROM Post p "
    + "JOIN FETCH p.comments c"_
    + "JOIN FETCH c.like l ")*/
   // Optional<Post> findPostByCommen(Long id, Long commenter, Long like, Pageable pageable);
    //Optional<Post> findPostByCommentAndIdAndLikesAndId(Long id, Long commenter, Long like, Pageable pageable);
}
