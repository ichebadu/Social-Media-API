package com.example.social_media_api.dto.reponse;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseOtp {
    private String message;
    private LocalDateTime localDateTime;
}
