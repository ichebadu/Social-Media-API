package com.example.social_media_api.service;

import com.example.social_media_api.dto.reponse.LikeResponse;
import com.example.social_media_api.dto.reponse.LoginResponse;
import com.example.social_media_api.dto.reponse.RegistrationResponse;
import com.example.social_media_api.dto.reponse.UserResponse;
import com.example.social_media_api.dto.request.LoginRequest;
import com.example.social_media_api.dto.request.RegistrationRequest;
import com.example.social_media_api.entity.User;
import com.example.social_media_api.utils.UserPage;
import com.example.social_media_api.utils.UserSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    RegistrationResponse registerUser(RegistrationRequest registrationRequest);

    LoginResponse authenticate(LoginRequest loginRequest);

    String uploadProfilePicture(MultipartFile file);

    List<String> getFollowers();

    List<String> getFollowing();

    String followOrUnfollowUser(String otherUserEmail, boolean follow);

    void validateUserExistence(String email);
    List<UserResponse> getAllUsers();


    User getUserByEmail(String email);

    Page<User> getUser (UserPage userPage,
                        UserSearchCriteria userSearchCriteria);

}
