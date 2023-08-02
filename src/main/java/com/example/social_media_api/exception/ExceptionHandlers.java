package com.example.social_media_api.exception;


import com.example.social_media_api.dto.reponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<ExceptionResponse>> invalid(InvalidCredentialsException e){
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(e.getMessage())
                .localDateTime(LocalDateTime.now())
                .build();
        ApiResponse<ExceptionResponse> apiResponse = new ApiResponse<>(exceptionResponse);
        return new ResponseEntity<>(apiResponse,HttpStatus.CONFLICT);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<ExceptionResponse>> UserNotFound(UserNotFoundException e){
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .localDateTime(LocalDateTime.now())
                .build();
        ApiResponse<ExceptionResponse> apiResponse = new ApiResponse<>(exceptionResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserDisabledException.class)
    public ResponseEntity<ApiResponse<ExceptionResponse>> userDisabled(UserDisabledException e){
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(e.getMessage())
                .localDateTime(LocalDateTime.now())
                .build();
        ApiResponse<ExceptionResponse> apiResponse = new ApiResponse<>(exceptionResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

}
