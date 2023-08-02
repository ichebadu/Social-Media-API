package com.example.social_media_api.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    public static String getUserEmailFromContext(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
