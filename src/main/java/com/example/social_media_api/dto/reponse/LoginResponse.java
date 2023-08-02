package com.example.social_media_api.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginResponse {
    private String username;
    private String email;
    private String accessToken;
    private String refreshToken;
    private String imageUrl;
}
