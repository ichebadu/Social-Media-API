package com.example.social_media_api.service.serviceImpl;

import com.example.social_media_api.entity.Otp;
import com.example.social_media_api.entity.User;
import com.example.social_media_api.notificationEvent.registrationEvent.UserRegistrationEvent;
import com.example.social_media_api.exception.InvalidCredentialsException;
import com.example.social_media_api.exception.OtpException;
import com.example.social_media_api.repository.NotificationRepository;
import com.example.social_media_api.repository.UserRepository;
import com.example.social_media_api.service.NotificationService;
import com.example.social_media_api.utils.RandomGeneratedValue;
import com.example.social_media_api.utils.VerifyUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final VerifyUser verifyUser;
    private final UserRepository userRepository;
    @Override
    public String verifyUserOtp(String email, String otp) {
        User user = verifyUser.verifyUserByEmail(email);

        log.info("Verifying OTP: " + user.getEmail());
        Otp otpConfirmation = notificationRepository.findByUser_EmailAndOtp(user.getEmail(), otp);
        System.out.println(otpConfirmation);

        if (otpConfirmation == null && isOtpExpired(otpConfirmation)) {
            throw new InvalidCredentialsException("invalid or Expired credential");

        }
        log.info(otpConfirmation.getUser().toString());
        user.setStatus(true);
        userRepository.save(user);
        return "Activated";

    }
    @Override
    public void sendOtp(User user, String otp, Otp newOtp){
        Otp foundOtp = notificationRepository.findByUserId(user.getId());

        if(foundOtp != null){
            notificationRepository.delete(foundOtp);
        }
        notificationRepository.save(newOtp);
        log.info(otp);
        applicationEventPublisher.publishEvent(new UserRegistrationEvent(user,otp));
    }

    @Override
    public boolean isOtpExpired(Otp otp){
        LocalDateTime otpCreatedAt = otp.getOtpExpiresAt();
        LocalDateTime currentDateTime = LocalDateTime.now();
        Duration duration = Duration.between(otpCreatedAt, currentDateTime);
        long minutesPassed = duration.toMinutes();
        long otpExpiresAt = 4;
        return minutesPassed > otpExpiresAt;
    }
    @Override
    public void saveOtp(Otp otp) {
        notificationRepository.save(otp);
    }
    @Override
    public Otp generateOtp(User user) {
        String otp = RandomGeneratedValue.generateRandomValues();
        return new Otp(otp, user);
    }
    @Override
    public String resendOtp(String email) {
        User user;
        try {
            user = verifyUser.verifyUserByEmail(email);

            String generateOtp = RandomGeneratedValue.generateRandomValues();
            Otp newOtp = generateOtp(user);

            sendOtp(user, generateOtp, newOtp);
            return generateOtp;

        } catch (Exception e) {
            log.info("Error resending OTP: ", e.getMessage());
            throw new OtpException("Error resending otp");
        }
    }
}