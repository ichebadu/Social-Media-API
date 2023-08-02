package com.example.social_media_api.service;

import com.example.social_media_api.dto.reponse.ResponseOtp;
import com.example.social_media_api.entity.Otp;
import com.example.social_media_api.entity.User;

public interface OtpService {
    String verifyUserOtp(String email, String otp);
    void saveOtp(Otp otp);
    ResponseOtp resendOtp(String email);
    void sendOtp(User user, String otp, Otp newOtp);
    Otp generateOtp(User user);
}
