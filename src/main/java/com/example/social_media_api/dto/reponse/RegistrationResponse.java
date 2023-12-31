package com.example.social_media_api.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class RegistrationResponse {
    private String username;
    private String message;
}
