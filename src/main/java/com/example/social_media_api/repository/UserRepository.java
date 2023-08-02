package com.example.social_media_api.repository;

import com.example.social_media_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByFollowers(User user);
    List<User> findByFollowing(User user);
}
