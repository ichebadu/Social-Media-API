package com.example.social_media_api.repository;

import com.example.social_media_api.entity.ConfirmationToken;
import com.example.social_media_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    ConfirmationToken findByUser_EmailAndOtp(String email, String otp);
    ConfirmationToken findByUserId(Long id);
    ConfirmationToken findAllByUser (User user);

    Optional<ConfirmationToken> findByOtp(String token);
}
