package com.example.social_media_api.controller;

import com.example.social_media_api.dto.reponse.ApiResponse;
import com.example.social_media_api.dto.reponse.RegistrationResponse;
import com.example.social_media_api.dto.reponse.ResponseOtp;
import com.example.social_media_api.dto.request.OtpVerificationRequest;
import com.example.social_media_api.dto.request.RegistrationRequest;
import com.example.social_media_api.service.OtpService;
import com.example.social_media_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/social-media-api/auth")
public class UserController {
    private final OtpService otpService;
    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<ApiResponse<RegistrationResponse>> registerUser(@RequestBody  RegistrationRequest request){
        ApiResponse<RegistrationResponse> apiResponse = new ApiResponse<>(userService.registerUser(request));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @PostMapping("/verification")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody OtpVerificationRequest request){
        ApiResponse<String> apiResponse = new ApiResponse<>(otpService.verifyUserOtp(request.getEmail(), request.getOtp()));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
