package com.example.social_media_api.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    @Column(name="username", nullable = false)
    private String username;
    @NotNull
    @NotEmpty(message = "Email address must not be empty")
    @Size(min = 5, max = 50)
    @Email(message = "Enter email address")
    private String email;

//    @Column(name = "image_url")
//    private String imageURL;
    @NotNull
    @NotEmpty(message = "Enter your password")
    private String password;
}
