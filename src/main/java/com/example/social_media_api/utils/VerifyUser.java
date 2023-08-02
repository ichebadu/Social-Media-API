package com.example.social_media_api.utils;

import com.example.social_media_api.entity.User;
import com.example.social_media_api.exception.UserNotFoundException;
import com.example.social_media_api.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@RequiredArgsConstructor
@Service
public class VerifyUser {
    private final UserRepository userRepository;

    public User verifyUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not Found"));
    }
}
