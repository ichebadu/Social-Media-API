package com.example.social_media_api.service.serviceImpl;

import com.example.social_media_api.config.CloudinaryConfig;
import com.example.social_media_api.dto.reponse.LikeResponse;
import com.example.social_media_api.dto.reponse.LoginResponse;
import com.example.social_media_api.dto.reponse.RegistrationResponse;
import com.example.social_media_api.dto.request.LoginRequest;
import com.example.social_media_api.dto.request.RegistrationRequest;
import com.example.social_media_api.entity.Otp;
import com.example.social_media_api.entity.Post;
import com.example.social_media_api.entity.User;
import com.example.social_media_api.enums.Role;
import com.example.social_media_api.exception.*;
import com.example.social_media_api.notificationEvent.PostLikesAndCommentNotification.PostNotificationService;
import com.example.social_media_api.notificationEvent.registrationEvent.UserRegistrationEvent;
import com.example.social_media_api.repository.PostRepository;
import com.example.social_media_api.repository.UserCriteriaRepository;
import com.example.social_media_api.repository.UserRepository;
import com.example.social_media_api.security.JwtService;
import com.example.social_media_api.service.NotificationService;
import com.example.social_media_api.service.UserService;
import com.example.social_media_api.utils.UserPage;
import com.example.social_media_api.utils.UserSearchCriteria;
import com.example.social_media_api.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher publisher;
    private final JwtService jwtService;
    private final PostRepository postRepository;
    private final PostNotificationService postNotificationService;
    private final UserCriteriaRepository userCriteriaRepository;


    @Override
    public RegistrationResponse registerUser(RegistrationRequest registrationRequest) {
        validateUserExistence(registrationRequest.getEmail());
        System.out.println(registrationRequest.getPassword());
        User user = registrationRequestToAppUser(registrationRequest);
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(Role.USER);
        user.setImageLinkUrl("http://res.cloudinary.com/dknryxg72/image/upload/c_fill,h_250,w_200/image_id6495085e37060874b1d34270");
        user.setStatus(false);

        var saveUser = userRepository.save(user);

        Otp otpEntity = notificationService.generateOtp(saveUser);
        otpEntity.setUser(user);
        notificationService.saveOtp(otpEntity);
        String otp = otpEntity.getOtp();
        publisher.publishEvent(new UserRegistrationEvent(user, otp));

        return RegistrationResponse.builder()
                .username(user.getUsername())
                .message("Registration Successful")
                .build();
    }

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        User user = getUserByEmail(loginRequest.getEmail());
         if(user.getStatus().equals(false)){
             throw new UserDisabledException("Password do not match");
         }
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),user.getPassword()
        );

         String jwtAccessToken = jwtService.generateToken(user);
         String jwtRefreshToken = jwtService.generateRefreshToken(user);
         log.info("PRINT OUT THE VALUE OF jwtrefreshetoken : " + jwtRefreshToken);
         log.info("PRINT OUT THE VALUE OF jwtAccesstoken: "  + jwtAccessToken);
         System.out.println(jwtRefreshToken);
         System.out.println("****************");
         System.out.println(jwtAccessToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return LoginResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .imageUrl(UserUtils.IMAGE_PLACEHOLDER_URL)
                .build();
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

    @Override
    public List<String> getFollowers(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<User> followers = userRepository.findByFollowing(user);
        return followers.stream().map(User::getEmail).collect(Collectors.toList());
    }

    @Override
    public List<String> getFollowing(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        List<User> following = userRepository.findByFollowers(user);
        return following.stream().map(User::getEmail).collect(Collectors.toList());
    }

    @Override
    public String followOrUnfollowUser(String email, String otherUserEmail, boolean follow) {
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        User otherUser = userRepository.findByEmail(otherUserEmail)
                .orElseThrow(() -> new UserNotFoundException("Other user not found"));

        if (currentUser.equals(otherUser)) {
            return "Cannot follow/unfollow yourself";
        }

        if (follow) {
            if (!currentUser.getFollowing().contains(otherUser)) {
                currentUser.getFollowing().add(otherUser);
                userRepository.save(currentUser);
                return "Followed " + otherUser.getEmail();
            } else {
                return "Already following " + otherUser.getEmail();
            }
        } else {
            if (currentUser.getFollowing().contains(otherUser)) {
                currentUser.getFollowing().remove(otherUser);
                userRepository.save(currentUser);
                return "Unfollowed " + otherUser.getEmail();
            } else {
                return "Not following " + otherUser.getEmail();
            }
        }
    }

    private void notifyPostLiked(User postOwner, User liker, Post post){
        String subject = "Post Linked Notification";
        String message = String.format("USER %s liked your post with ID: %d", liker.getUsername(), post.getId());
        postNotificationService.SendEmail(postOwner.getEmail(),subject,message );
    }
    @Override
    public void validateUserExistence(String email){
        if(userRepository.existsByEmail(email)){
            throw new UserAlreadyExistsException(email);
        }
    }
    @Override
    public User registrationRequestToAppUser(RegistrationRequest registrationRequest) {
        return modelMapper.map(registrationRequest, User.class);
    }
    @Override
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User Not Found"));
    }
    @Override
    public LikeResponse likeOrUnlike(String email, Long postId, boolean like) {
        User user = getUserByEmail(email);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        boolean userAlreadyLiked = post.getLikes().contains(user);

        if ((like && !userAlreadyLiked) || (!like && userAlreadyLiked)) {
            if (like) {
                post.getLikes().add(user);
                post.setLikesCount(post.getLikesCount() + 1);
            } else {
                post.getLikes().remove(user);
                post.setLikesCount(post.getLikesCount() - 1);
            }
            postRepository.save(post);
            String action = like ? "Liked " : "Unliked ";
            notifyPostLiked(post.getUser(), user, post);
            return LikeResponse.builder()
                    .username(user.getUsername())
                    .message(action + "post with ID: " + postId)
                    .build();
        } else {
            String action = like ? "liked " : "unliked ";
            return LikeResponse.builder()
                    .username(user.getUsername())
                    .message("Already " + action + "the post with ID: " + postId)
                    .build();
        }
    }
    @Override
    public Page<User> getUser (UserPage userPage,
                               UserSearchCriteria userSearchCriteria){
        return userCriteriaRepository.findAllWithFilter(userPage, userSearchCriteria);

    }

    @Override
    public User addUser(User user){
        return userRepository.save(user);
    }
}