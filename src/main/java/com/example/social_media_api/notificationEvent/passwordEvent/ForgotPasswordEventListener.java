package com.example.social_media_api.notificationEvent.passwordEvent;

import com.example.social_media_api.config.MailConfig;
import com.example.social_media_api.entity.ConfirmationToken;
import com.example.social_media_api.entity.User;
import com.example.social_media_api.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordEventListener implements ApplicationListener<UserForgotPasswordEvent> {
    private final MailConfig mailConfig;
    private final AuthenticationService authenticationService;

    @Override
    public void onApplicationEvent(UserForgotPasswordEvent event) {
        String resetToken = UUID.randomUUID().toString();
        User user = event.getUser();

        ConfirmationToken confirmationToken = new ConfirmationToken(resetToken,user);
        authenticationService.saveOtp(confirmationToken);

        String url = event.getApplicationUrl() + "/api/v1/social-media-api/auth/verify-password-token?token=" + resetToken;
        log.info(url);

        try {
            sendResetPasswordEmail(user, url);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResetPasswordEmail(User user, String url) throws MessagingException {
        MimeMessage message = mailConfig.javaMailSender().createMimeMessage();
        messageDetails(user, url, message);
        mailConfig.javaMailSender().send(message);
        log.info("Password reset email sent to: " + user);
    }

    private void messageDetails(User user, String url, MimeMessage message) throws MessagingException {
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        messageHelper.setFrom("chukwu.iche@gmail.com");
        messageHelper.setSubject("Password Reset Request");
        messageHelper.setTo(user.getEmail());

        String senderName = "Ichebadu Hub Inc";
        String mailContent = "<div style='width:100%; background: #f8f8f8;'>"
                + "<p style='font-size: 18px;'>Hello, " + user.getUsername() + "</p>"
                + "<p style='font-size: 16px;'>You've requested a password reset for your account.</p>"
                + "<p style='font-size: 16px;'>Please click the link below to reset your password:</p>"
                + "<a href='" + url + "'>Reset Password</a>"
                + "<p style='font-size: 16px;'>If you didn't request this, you can ignore this email.</p>"
                + "<p style='font-size: 16px;'>Thank you,</p> "
                + "<p style='font-size: 16px;'> " + senderName + " +</p>"
                + "</div>";
        messageHelper.setText(mailContent, true);

        log.info(url);
        log.info("Sent token ", user);

    }
}
