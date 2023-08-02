package com.example.social_media_api.service;

import com.example.social_media_api.config.CloudinaryConfig;
import com.example.social_media_api.dto.reponse.LoginResponse;
import com.example.social_media_api.dto.reponse.RegistrationResponse;
import com.example.social_media_api.dto.request.LoginRequest;
import com.example.social_media_api.dto.request.RegistrationRequest;
import com.example.social_media_api.entity.Otp;
import com.example.social_media_api.entity.User;
import com.example.social_media_api.event.registrationEvent.UserRegistration;
import com.example.social_media_api.exception.UserAlreadyExistsException;
import com.example.social_media_api.exception.UserNotFoundException;
import com.example.social_media_api.repository.UserRepository;
import com.example.social_media_api.security.JwtService;
import com.example.social_media_api.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private ModelMapper modelMapper;
    private final OtpService otpService;
    private final ApplicationEventPublisher publisher;



    @Override
    public RegistrationResponse registerUser(RegistrationRequest registrationRequest) {
        validateUserExistence(registrationRequest.getEmail());
        User user = registrationRequestToAppUser(registrationRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setImageLinkUrl("http://res.cloudinary.com/dknryxg72/image/upload/c_fill,h_250,w_200/image_id6495085e37060874b1d34270");
        user.setStatus(false);

        var saveUser = userRepository.save(user);

        Otp otpEntity = otpService.generateOtp(saveUser);
        otpEntity.setUser(user);
        otpService.saveOtp(otpEntity);
        String otp = otpEntity.getOtp();
        publisher.publishEvent(new UserRegistration(user, otp));

        return RegistrationResponse.builder()
                .username(user.getUsername())
                .message("Registration Successful")
                .build();
    }

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public String uploadProfilePicture(MultipartFile file) {
        User user = userRepository.findByEmail(UserUtils.getUserEmailFromContext())
                .orElseThrow(()-> new UserNotFoundException("USER NOT FOUND"));
        CloudinaryConfig config = new CloudinaryConfig();
        String url = config.imageLink(file,user.getId().toString());
        user.setImageLinkUrl(url);
        userRepository.save(user);
        return url;
    }
    private void validateUserExistence(String email){
        if(userRepository.existsByEmail(email)){
            throw new UserAlreadyExistsException(email);
        }
    }
    private User registrationRequestToAppUser (RegistrationRequest registrationRequest) {
        return modelMapper.map(registrationRequest, User.class);
    }

    public User verifyUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not Found"));
    }
}
