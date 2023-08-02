package com.example.social_media_api.exception;

public class UserDisabledException extends RuntimeException{
    public UserDisabledException(String accountIsDisabled){
        super(accountIsDisabled);
    }
}
