package com.example.social_media_api.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;

public class LoginRequest {
    @Column(name="email", nullable = false)
    @NotEmpty(message = "Enter your email")
    private String email;
    @Column(name="password", nullable = false)
    @NotEmpty(message = "Enter your password")
    private String password;
}
