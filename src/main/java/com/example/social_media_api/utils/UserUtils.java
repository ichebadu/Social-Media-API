package com.example.social_media_api.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    public static final String IMAGE_PLACEHOLDER_URL = "http://res.cloudinary.com/dknryxg72/image/upload/c_fill,h_250,w_200/image_id6495085e37060874b1d34270";

    public static String getUserEmailFromContext(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
