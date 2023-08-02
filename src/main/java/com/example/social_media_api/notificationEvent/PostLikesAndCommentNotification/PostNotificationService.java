package com.example.social_media_api.notificationEvent.PostLikesAndCommentNotification;

import com.example.social_media_api.config.MailConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostNotificationService  {
    private final MailConfig mailConfig;

    public void SendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailConfig.javaMailSender().send(message);
    }
}
