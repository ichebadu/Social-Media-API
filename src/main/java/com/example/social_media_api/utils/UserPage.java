package com.example.social_media_api.utils;

import lombok.Data;
import org.springframework.data.domain.Sort;
@Data
public class UserPage {
    private int pageNumber = 0;
    private int pageSize = 10;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "username";

}
