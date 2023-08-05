package com.example.social_media_api.utils;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class PostPage {
    private int PageSize = 0;
    private int PageNumber = 10;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "title";
}
