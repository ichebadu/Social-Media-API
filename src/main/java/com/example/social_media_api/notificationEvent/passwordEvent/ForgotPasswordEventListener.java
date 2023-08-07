package com.example.social_media_api.notificationEvent.passwordEvent;

import com.example.social_media_api.config.MailConfig;
import com.example.social_media_api.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordEventListener implements ApplicationListener<UserForgotPasswordEvent> {
    private final MailConfig mailConfig;

    @Override
    public void onApplicationEvent(UserForgotPasswordEvent event) {
        String resetToken = event.getResetToken();
        User user = event.getUser();
        String email = user.getEmail();
        try {
            sendResetPasswordEmail(email, resetToken);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResetPasswordEmail(String user, String resetToken) throws MessagingException {
        MimeMessage message = mailConfig.javaMailSender().createMimeMessage();
        messageDetails(user, resetToken, message);
        mailConfig.javaMailSender().send(message);
        log.info("Password reset email sent to: " + user);
    }

    private void messageDetails(String user, String resetToken, MimeMessage message) throws MessagingException {
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        messageHelper.setFrom("chukwu.iche@gmail.com");
        messageHelper.setSubject("Password Reset Request");
        messageHelper.setTo(user);

        String mailContent = "<div style='width:100%; background: #f8f8f8;'>"
                + "<p style='font-size: 18px;'>Hello, " + user + "</p>"
                + "<p style='font-size: 16px;'>You've requested a password reset for your account.</p>"
                + "<p style='font-size: 16px;'>Please click the link below to reset your password:</p>"
                + "<a href='https://your-website.com/reset-password?token=" + resetToken + "'>Reset Password</a>"
                + "<p style='font-size: 16px;'>If you didn't request this, you can ignore this email.</p>"
                + "<p style='font-size: 16px;'>Thank you,</p>"
                + "<p style='font-size: 16px;'>Social Media Api</p>"
                + "</div>";
        messageHelper.setText(mailContent, true);
    }
}
