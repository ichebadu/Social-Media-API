package com.example.social_media_api.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostRequest {
    private String content;
    private int likesCount;
    private LocalDateTime creationDate;

}
