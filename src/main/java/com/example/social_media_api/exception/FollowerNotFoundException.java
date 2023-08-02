package com.example.social_media_api.exception;

public class FollowerNotFoundException extends RuntimeException {
    public FollowerNotFoundException(String message) {
        super(message);
    }
}
