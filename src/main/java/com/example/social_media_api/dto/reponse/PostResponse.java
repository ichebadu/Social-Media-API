package com.example.social_media_api.dto.reponse;

import com.example.social_media_api.entity.Post;
import lombok.*;


@Builder
@Setter
@Getter
public class PostResponse {
    private String message;
}
