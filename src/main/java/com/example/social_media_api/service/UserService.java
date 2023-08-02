package com.example.social_media_api.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserDetails loadByUsername(String username);
}
