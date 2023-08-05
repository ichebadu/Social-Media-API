package com.example.social_media_api.utils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class CommentPage {
    private int pageSize = 0;
    private int pageNumber = 10;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "content";
}
