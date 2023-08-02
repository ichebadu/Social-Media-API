package com.example.social_media_api.exception;

public class FollowingNotFoundException extends RuntimeException {
    public FollowingNotFoundException(String message) {
        super(message);
    }
}
