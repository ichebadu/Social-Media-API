package com.example.social_media_api.service;

import com.example.social_media_api.dto.request.ResetPasswordRequest;
import com.example.social_media_api.entity.ConfirmationToken;
import com.example.social_media_api.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    String verifyUserOtp(String email, String otp);

    boolean isOtpExpired(ConfirmationToken confirmationToken);

    void saveOtp(ConfirmationToken confirmationToken);
    String resendOtp(String email);
    void sendOtp(User user, String otp, ConfirmationToken newConfirmationToken);
    ConfirmationToken generateOtp(User user);

    String verifyToken(String token);

    String forgotPassword(String email, HttpServletRequest request);

    String resetPassword(ResetPasswordRequest resetPasswordRequest, String token);
}
