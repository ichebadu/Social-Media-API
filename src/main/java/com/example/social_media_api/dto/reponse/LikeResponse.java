package com.example.social_media_api.dto.reponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeResponse {
    private String message;
    private String username;
}
