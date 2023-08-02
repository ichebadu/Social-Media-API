package com.example.social_media_api.controller;

import com.example.social_media_api.dto.reponse.ApiResponse;
import com.example.social_media_api.dto.reponse.LikeResponse;
import com.example.social_media_api.dto.reponse.LoginResponse;
import com.example.social_media_api.dto.reponse.RegistrationResponse;
import com.example.social_media_api.dto.request.LoginRequest;
import com.example.social_media_api.dto.request.OtpVerificationRequest;
import com.example.social_media_api.dto.request.RegistrationRequest;
import com.example.social_media_api.service.NotificationService;
import com.example.social_media_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/social-media-api/auth")
public class UserController {
    private final NotificationService notificationService;
    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<ApiResponse<RegistrationResponse>> registerUser(@RequestBody  RegistrationRequest request){
        ApiResponse<RegistrationResponse> apiResponse = new ApiResponse<>(userService.registerUser(request));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @PostMapping("/verification")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody OtpVerificationRequest request){
        ApiResponse<String> apiResponse = new ApiResponse<>(notificationService.verifyUserOtp(request.getEmail(), request.getOtp()));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request){
        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>(userService.authenticate(request));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/resend-otp")
    public ResponseEntity<ApiResponse<String>> resend(@RequestParam("email") String email){
        ApiResponse<String> apiResponse = new ApiResponse<>(notificationService.resendOtp(email));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @PutMapping("/follow-or-unfollow")
    public ResponseEntity<ApiResponse<String>> followOrUnfollowUser(@RequestParam("email") String email,
                                                                    @RequestParam("otherUserEmail") String otherUserEmail,
                                                                    @RequestParam("follow") boolean follow) {
        ApiResponse<String> apiResponse = new ApiResponse<>(userService.followOrUnfollowUser(email, otherUserEmail, follow));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/upload-picture",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> profilePic(@RequestParam("file") MultipartFile file){
        ApiResponse<String> apiResponse = new ApiResponse<>(userService.uploadProfilePicture(file));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/following")
    public ResponseEntity<ApiResponse<List<String>>> getFollowing(String email) {
        ApiResponse<List<String>> apiResponse = new ApiResponse<>(userService.getFollowing(email));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/like-or-unlike")
    public ResponseEntity<ApiResponse<LikeResponse>> likeOrUnlike(@RequestParam("email") String email,
                                                                  @RequestParam("postId") Long postId,
                                                                  @RequestParam("like") boolean like) {
        ApiResponse<LikeResponse> apiResponse = new ApiResponse<>(userService.likeOrUnlike(email, postId, like));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/follower")
    public ResponseEntity<ApiResponse<List<String>>> getFollowers(String email){
        ApiResponse<List<String>> apiResponse = new ApiResponse<>(userService.getFollowers(email));
        return  new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
}
