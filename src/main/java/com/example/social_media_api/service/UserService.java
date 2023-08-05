package com.example.social_media_api.service;

import com.example.social_media_api.dto.reponse.LikeResponse;
import com.example.social_media_api.dto.reponse.LoginResponse;
import com.example.social_media_api.dto.reponse.RegistrationResponse;
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

    List<String> getFollowers(String userEmail);

    List<String> getFollowing(String userEmail);

    String followOrUnfollowUser(String email, String otherUserEmail, boolean follow);

    void validateUserExistence(String email);

    User registrationRequestToAppUser(RegistrationRequest registrationRequest);

    User getUserByEmail(String email);

    LikeResponse likeOrUnlike(String email, Long postId, boolean like);

    Page<User> getUser (UserPage userPage,
                        UserSearchCriteria userSearchCriteria);

    User addUser(User user);
}
