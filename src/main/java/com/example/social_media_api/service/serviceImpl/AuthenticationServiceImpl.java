package com.example.social_media_api.service.serviceImpl;

import com.example.social_media_api.dto.request.ResetPasswordRequest;
import com.example.social_media_api.entity.ConfirmationToken;
import com.example.social_media_api.entity.User;
import com.example.social_media_api.exception.SamePasswordException;
import com.example.social_media_api.exception.UserNotFoundException;
import com.example.social_media_api.notificationEvent.passwordEvent.UserForgotPasswordEvent;
import com.example.social_media_api.notificationEvent.registrationEvent.UserRegistrationEvent;
import com.example.social_media_api.exception.InvalidCredentialsException;
import com.example.social_media_api.exception.OtpException;
import com.example.social_media_api.repository.ConfirmationTokenRepository;
import com.example.social_media_api.repository.UserRepository;
import com.example.social_media_api.service.AuthenticationService;
import com.example.social_media_api.utils.EmailUtils;
import com.example.social_media_api.utils.RandomGeneratedValue;
import com.example.social_media_api.utils.VerifyUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final VerifyUser verifyUser;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public String verifyUserOtp(String email, String otp) {
        User user = verifyUser.verifyUserByEmail(email);

        log.info("Verifying OTP: " + user.getEmail());
        ConfirmationToken confirmationTokenConfirmation = confirmationTokenRepository.findByUser_EmailAndOtp(user.getEmail(), otp);
        System.out.println(confirmationTokenConfirmation);

        if (confirmationTokenConfirmation == null && isOtpExpired(confirmationTokenConfirmation)) {
            throw new InvalidCredentialsException("invalid or Expired credential");

        }
        log.info(confirmationTokenConfirmation.getUser().toString());
        user.setStatus(true);
        userRepository.save(user);
        return "Activated";

    }
    @Override
    public void sendOtp(User user, String otp, ConfirmationToken newConfirmationToken){
        ConfirmationToken foundConfirmationToken = confirmationTokenRepository.findByUserId(user.getId());

        if(foundConfirmationToken != null){
            confirmationTokenRepository.delete(foundConfirmationToken);
        }
        confirmationTokenRepository.save(newConfirmationToken);
        log.info(otp);
        applicationEventPublisher.publishEvent(new UserRegistrationEvent(user,otp));
    }

    @Override
    public boolean isOtpExpired(ConfirmationToken confirmationToken){
        LocalDateTime otpCreatedAt = confirmationToken.getOtpExpiresAt();
        LocalDateTime currentDateTime = LocalDateTime.now();
        Duration duration = Duration.between(otpCreatedAt, currentDateTime);
        long minutesPassed = duration.toMinutes();
        long otpExpiresAt = 4;
        return minutesPassed > otpExpiresAt;
    }
    @Override
    public void saveOtp(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }
    @Override
    public ConfirmationToken generateOtp(User user) {
        String otp = RandomGeneratedValue.generateRandomValues();
        return new ConfirmationToken(otp, user);
    }
    @Override
    public String resendOtp(String email) {
        User user;
        try {
            user = verifyUser.verifyUserByEmail(email);

            String generateOtp = RandomGeneratedValue.generateRandomValues();
            ConfirmationToken newConfirmationToken = generateOtp(user);

            sendOtp(user, generateOtp, newConfirmationToken);
            return generateOtp;

        } catch (Exception e) {
            log.info("Error resending OTP: ", e.getMessage());
            throw new OtpException("Error resending otp");
        }
    }
    @Override
    public String verifyToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByOtp(token)
                .orElseThrow(()-> new UserNotFoundException("Invalid Credential"));
        if(isOtpExpired(confirmationToken)){
            confirmationTokenRepository.delete(confirmationToken);
            throw new OtpException("OTP is Expired");
        }
        return confirmationToken.getUser().getEmail();
    }

    @Override
    public String forgotPassword(String email, HttpServletRequest request){
        User user = verifyUser.verifyUserByEmail(email);
        ConfirmationToken confirmationToken = confirmationTokenRepository.findAllByUser(user);
        if(confirmationToken !=null){
            confirmationTokenRepository.delete(confirmationToken);
        }
        applicationEventPublisher.publishEvent(new UserForgotPasswordEvent(user, EmailUtils.backEndAppUrl(request)));
        return "Please Check Your Mail for Password Reset Link";
    }
    @Override
    public String resetPassword(ResetPasswordRequest resetPasswordRequest, String token){
        String email = verifyToken(token);
        User user = verifyUser.verifyUserByEmail(email);
        if(passwordEncoder.matches(resetPasswordRequest.getPassword(), user.getPassword())){
            throw new SamePasswordException("Please choose a different password");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        return "Password changed successfully";
    }
}