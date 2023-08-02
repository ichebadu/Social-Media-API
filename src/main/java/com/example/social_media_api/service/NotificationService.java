package com.example.social_media_api.service;

import com.example.social_media_api.entity.Otp;
import com.example.social_media_api.entity.User;

public interface NotificationService {
    String verifyUserOtp(String email, String otp);
    void saveOtp(Otp otp);
    String resendOtp(String email);
    void sendOtp(User user, String otp, Otp newOtp);
    Otp generateOtp(User user);
}