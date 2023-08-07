package com.example.social_media_api.utils;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class PostPage {
    private int pageSize = 10; // Change default page size to 10 (or your desired value)
    private int pageNumber = 0; // Change default page number to 0 (or your desired value)
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "title";
}
