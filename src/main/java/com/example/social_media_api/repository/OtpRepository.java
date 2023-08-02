package com.example.social_media_api.repository;

import com.example.social_media_api.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Otp findByUser_EmailAndOtp(String email, String otp);

    Otp findByUserId(Long id);
}
