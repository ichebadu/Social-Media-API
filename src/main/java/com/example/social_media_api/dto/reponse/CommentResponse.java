package com.example.social_media_api.dto.reponse;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private String content;
    private LocalDateTime createdAt;


}
