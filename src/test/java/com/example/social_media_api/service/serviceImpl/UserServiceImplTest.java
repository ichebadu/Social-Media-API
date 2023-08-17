package com.example.social_media_api.service.serviceImpl;

import com.example.social_media_api.dto.reponse.RegistrationResponse;
import com.example.social_media_api.dto.request.RegistrationRequest;
import com.example.social_media_api.entity.ConfirmationToken;
import com.example.social_media_api.entity.User;
import com.example.social_media_api.enums.Role;
import com.example.social_media_api.exception.UserAlreadyExistsException;
import com.example.social_media_api.repository.UserRepository;
import com.example.social_media_api.security.JwtService;
import com.example.social_media_api.service.AuthenticationService;
import com.example.social_media_api.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private UserServiceImpl userService;
    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> {
            String rawPassword = invocation.getArgument(0);
            return "encoded_" + rawPassword;
        });
    }
    @Test
    void register(){

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("chiorlujack@gmail.com");
        registrationRequest.setUsername("jwsven");
        registrationRequest.setPassword("12345");

        when(userRepository.existsByEmail("chiorlujack@gmail.com")).thenReturn(false);

        User savedUser = new User();
        savedUser.setUsername(registrationRequest.getUsername());
        savedUser.setEmail(registrationRequest.getEmail());
        savedUser.setRole(Role.USER);
        savedUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        savedUser.setImageLinkUrl(UserUtils.IMAGE_PLACEHOLDER_URL);
        savedUser.setStatus(false);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        when(authenticationService.generateOtp(any(User.class))).thenReturn(new ConfirmationToken());

        RegistrationResponse response = userService.registerUser(registrationRequest);

        assertEquals("chiorlujack@gmail.com", response.getUsername());
        assertEquals("Registration Successful", response.getMessage());
    }

    @Test
    void registerUserUser_alreadyExist() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("chiorlujack@gmail.com");
        registrationRequest.setUsername("testuser");
        registrationRequest.setPassword("testpassword");

        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(true);
        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(registrationRequest));

    }

//    @Test
//    void authenticate() {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail("chiorlujack@gmail.com");
//        loginRequest.setPassword("12345");
//
//        User user = new User();
//        user.setEmail(loginRequest.getEmail());
//        user.setPassword(passwordEncoder.encode(loginRequest.getPassword())); // Encoded password
//        user.setStatus(true);
//        user.setRole(Role.USER);
//
//        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(java.util.Optional.of(user));
//
//        String jwtAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImNoaW9ybHVqYWNrQGdtYWlsLmNvbSIsImlhdCI6MTY5MTMxODMxOSwiZXhwIjoxNjkxNDA0NzE5fQ.VVQowkluzlH1u7fvemSSCMXvRYRPN616a9lFMdpHUKE";
//        String jwtRefreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGlvcmx1amFja0BnbWFpbC5jb20iLCJpYXQiOjE2OTEzMTgzMTksImV4cCI6MTY5MTU3NzUxOX0.CWJadNehhokjulqic7kf6XSvs2ldFx4AZ9aZXaR8g_8";
//
//        when(jwtService.generateToken(user)).thenReturn(jwtAccessToken);
//        when(jwtService.generateRefreshToken(user)).thenReturn(jwtRefreshToken);
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//                loginRequest.getEmail(), user.getPassword()
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        LoginResponse response = userService.authenticate(loginRequest);
//
//        assertEquals(loginRequest.getEmail(), response.getEmail());
//        assertEquals(jwtAccessToken, response.getAccessToken());
//        assertEquals(jwtRefreshToken, response.getRefreshToken());
//    }

//    @Test
//    void uploadProfilePicture() {
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//                "chiorlujack@gmail.com", "12345"
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String userEmail = "chiorlujack@gmail.com";
//        UserUtils userUtils = mock(UserUtils.class);
//        when(userUtils.getUserEmailFromContext()).thenReturn(userEmail);
//
//        // Mock UserRepository
//        UserRepository userRepository = mock(UserRepository.class);
//        byte[] fileContent = "Sample image data".getBytes();
//        MultipartFile file = new MockMultipartFile("image.jpg", fileContent);
//        User user = new User();
//        user.setId(167L);
//        user.setEmail("chiorlujack@gmail.com");
//        when(userRepository.findByEmail(eq("chiorlujack@gmail.com"))).thenReturn(Optional.of(user));
//
//        // Mock CloudinaryConfig.imageLink behavior
//        CloudinaryConfig config = mock(CloudinaryConfig.class);
//        String expectedUrl = "https://example.com/image.jpg"; // Replace with your expected URL
//        when(config.imageLink(eq(file), eq(user.getId().toString()))).thenReturn(expectedUrl);
//
//        // Execute the method under test
//        String resultUrl = userService.uploadProfilePicture(file);
//
//        // Verify that userRepository.save was called with the correct user
//        verify(userRepository).save(user);
//
//        // Assert the returned URL is as expected
//        assertEquals(expectedUrl, resultUrl);
//    }
//
//    @Test
//    @WithMockUser(username = "chiorlujack@gmail.com", password = "12345")
//    void followOrUnfollowUser() {
//        UserRepository userRepository = mock(UserRepository.class);
//        when(userRepository.findByEmail(eq("user1@example.com"))).thenReturn(Optional.of(new User()));
//        when(userRepository.findByEmail(eq("user2@example.com"))).thenReturn(Optional.of(new User()));
//        when(userRepository.findByEmail(eq("nonexistent@example.com"))).thenReturn(Optional.empty());
//
//
//
//        String followResult = userService.followOrUnfollowUser("user2@example.com", true);
//        assertEquals("Followed user2@example.com", followResult);
//
//
//        String alreadyFollowingResult = userService.followOrUnfollowUser("user2@example.com", true);
//        assertEquals("Already following user2@example.com", alreadyFollowingResult);
//
//
//        String unfollowResult = userService.followOrUnfollowUser("user2@example.com", false);
//        assertEquals("Unfollowed user2@example.com", unfollowResult);
//
//
//        String notFollowingResult = userService.followOrUnfollowUser("user2@example.com", false);
//        assertEquals("Not following user2@example.com", notFollowingResult);
//
//
//        String selfFollowResult = userService.followOrUnfollowUser("user1@example.com", true);
//        assertEquals("Cannot follow/unfollow yourself", selfFollowResult);
//
//
//        String userNotFoundResult = userService.followOrUnfollowUser("nonexistent@example.com", true);
//        assertEquals("Other user not found", userNotFoundResult);
//    }
    public static String getUserEmailFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;

    }

}