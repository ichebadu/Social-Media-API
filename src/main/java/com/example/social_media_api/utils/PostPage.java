package com.example.social_media_api.utils;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class PostPage {
    private int pageSize = 10;
    private int pageNumber = 0;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "title";
}
