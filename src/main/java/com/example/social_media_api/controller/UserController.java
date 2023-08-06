package com.example.social_media_api.controller;

import com.example.social_media_api.dto.reponse.ApiResponse;
import com.example.social_media_api.dto.reponse.LikeResponse;
import com.example.social_media_api.dto.reponse.LoginResponse;
import com.example.social_media_api.dto.reponse.RegistrationResponse;
import com.example.social_media_api.dto.request.LoginRequest;
import com.example.social_media_api.dto.request.OtpVerificationRequest;
import com.example.social_media_api.dto.request.RegistrationRequest;
import com.example.social_media_api.entity.User;
import com.example.social_media_api.service.NotificationService;
import com.example.social_media_api.service.UserService;
import com.example.social_media_api.utils.UserPage;
import com.example.social_media_api.utils.UserSearchCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/social-media-api/auth")
@Tag(name = "Authentication and User Actions")
public class UserController {
    private final NotificationService notificationService;
    private final UserService userService;

    @PostMapping("/registration")
    @Operation(
            summary = "User Registration REST API",
            description = "User Registration REST API is used to register a new user"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Http status 200 SUCCESS")
    public ResponseEntity<ApiResponse<RegistrationResponse>> registerUser(@RequestBody RegistrationRequest request) {
        ApiResponse<RegistrationResponse> apiResponse = new ApiResponse<>(userService.registerUser(request));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/verification")
    @Operation(
            summary = "User Verification REST API",
            description = "User Verification REST API is used to verify user's email using OTP"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Http status 200 SUCCESS")
    public ResponseEntity<ApiResponse<String>> verifyUser(@RequestBody OtpVerificationRequest request) {
        ApiResponse<String> apiResponse = new ApiResponse<>(notificationService.verifyUserOtp(request.getEmail(), request.getOtp()));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/login")
    @Operation(
            summary = "User Login REST API",
            description = "User Login REST API is used for user authentication"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Http status 200 SUCCESS")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>(userService.authenticate(request));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/resend-otp")
    @Operation(
            summary = "Resend OTP REST API",
            description = "Resend OTP REST API is used to resend OTP for email verification"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Http status 200 SUCCESS")
    public ResponseEntity<ApiResponse<String>> resendOtp(@RequestParam("email") String email) {
        ApiResponse<String> apiResponse = new ApiResponse<>(notificationService.resendOtp(email));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/follow-or-unfollow")
    @Operation(
            summary = "Follow/Unfollow User REST API",
            description = "Follow/Unfollow User REST API is used to follow or unfollow another user"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Http status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> followOrUnfollowUser(
            @RequestParam("otherUserEmail") String otherUserEmail,
            @RequestParam("follow") boolean follow) {
        ApiResponse<String> apiResponse = new ApiResponse<>(userService.followOrUnfollowUser(otherUserEmail, follow));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @GetMapping(value = "/upload-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload Profile Picture REST API",
            description = "Upload Profile Picture REST API is used to upload a user's profile picture"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Http status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        ApiResponse<String> apiResponse = new ApiResponse<>(userService.uploadProfilePicture(file));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/following")
    @Operation(
            summary = "Get Following Users REST API",
            description = "Get Following Users REST API is used to get the list of users followed by a user"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Http status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<String>>> getFollowing() {
        ApiResponse<List<String>> apiResponse = new ApiResponse<>(userService.getFollowing());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }



    @GetMapping("/follower")
    @Operation(
            summary = "Get Followers REST API",
            description = "Get Followers REST API is used to get the list of users following a user"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Http status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<String>>> getFollowers() {
        ApiResponse<List<String>> apiResponse = new ApiResponse<>(userService.getFollowers());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            summary = "Get Users REST API",
            description = "Get Users REST API is used to get a paginated list of users based on search criteria"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Http status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Page<User>>> getUsers(@Parameter(hidden = true) UserPage userPage,
                                                            @Parameter(hidden = true) UserSearchCriteria userSearchCriteria) {
        ApiResponse<Page<User>> apiResponse = new ApiResponse<>(userService.getUser(userPage, userSearchCriteria));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping
    @Operation(
            summary = "Add User REST API",
            description = "Add User REST API is used to add a new user"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Http status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("isAuthenticated()")public ResponseEntity<ApiResponse<User>> addUser(@RequestBody User user) {
        ApiResponse<User> apiResponse = new ApiResponse<>(userService.addUser(user));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}

