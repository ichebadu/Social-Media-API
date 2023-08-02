package com.example.social_media_api.service;

import com.example.social_media_api.repository.UserRepository;
import com.example.social_media_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    @Override
    public UserDetails loadByUsername(String username) {
        return loadByUsername(username);
    }
}
