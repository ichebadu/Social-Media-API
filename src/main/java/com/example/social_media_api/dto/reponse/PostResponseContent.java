package com.example.social_media_api.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseContent {
    private String content;
    private String title;
    private int likesCount;
    private LocalDateTime createdAt;
}
