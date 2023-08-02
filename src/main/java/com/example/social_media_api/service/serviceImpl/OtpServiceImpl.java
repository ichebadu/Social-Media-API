package com.example.social_media_api.service.serviceImpl;

import com.example.social_media_api.dto.reponse.ResponseOtp;
import com.example.social_media_api.entity.Otp;
import com.example.social_media_api.entity.User;
import com.example.social_media_api.event.registrationEvent.UserRegistrationEvent;
import com.example.social_media_api.exception.InvalidCredentialsException;
import com.example.social_media_api.exception.OtpException;
import com.example.social_media_api.repository.OtpRepository;
import com.example.social_media_api.repository.UserRepository;
import com.example.social_media_api.service.OtpService;
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
public class OtpServiceImpl implements OtpService {
    private final OtpRepository otpRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final VerifyUser verifyUser;
    private final UserRepository userRepository;

    public String verifyUserOtp(String email, String otp) {
        User user = verifyUser.verifyUserByEmail(email);

        log.info("Verifying OTP: " + user.getEmail());
        Otp otpConfirmation = otpRepository.findByUser_EmailAndOtp(user.getEmail(), otp);
        System.out.println(otpConfirmation);

        if (otpConfirmation == null && isOtpExpired(otpConfirmation)) {
            throw new InvalidCredentialsException("invalid or Expired credential");

        }
        log.info(otpConfirmation.getUser().toString());
        user.setStatus(true);
        userRepository.save(user);
        return "Activated";

    }

    public void sendOtp(User user, String otp, Otp newOtp){
        Otp foundOtp = otpRepository.findByUserId(user.getId());

        if(foundOtp != null){
            otpRepository.delete(foundOtp);
        }
        otpRepository.save(newOtp);
        log.info(otp);
        applicationEventPublisher.publishEvent(new UserRegistrationEvent(user,otp));
    }

    public boolean isOtpExpired(Otp otp){
        LocalDateTime otpCreatedAt = otp.getOtpExpiresAt();
        LocalDateTime currentDateTime = LocalDateTime.now();
        Duration duration = Duration.between(otpCreatedAt, currentDateTime);
        long minutesPassed = duration.toMinutes();
        long otpExpiresAt = 4;
        return minutesPassed > otpExpiresAt;
    }

    public void saveOtp(Otp otp) {
        otpRepository.save(otp);
    }
    public Otp generateOtp(User user) {
        String otp = RandomGeneratedValue.generateRandomValues();
        return new Otp(otp, user);
    }
    public ResponseOtp resendOtp(String email) {
        User user;
        try {
            user = verifyUser.verifyUserByEmail(email);

            String generateOtp = RandomGeneratedValue.generateRandomValues();
            Otp newOtp = generateOtp(user);

            sendOtp(user, generateOtp, newOtp);

            return ResponseOtp.builder()
                    .message(generateOtp)
                    .localDateTime(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            log.info("Error resending OTP: ", e.getMessage());
            throw new OtpException("Error resending otp");
        }
    }
}