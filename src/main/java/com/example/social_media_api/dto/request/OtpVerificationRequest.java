package com.example.social_media_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OtpVerificationRequest {
    private String email;
    private String otp;
}
