package com.example.social_media_api.notificationEvent;

import com.example.social_media_api.config.MailConfig;
import com.example.social_media_api.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class CommentEventListener implements ApplicationListener<CommentNotification> {
    private final MailConfig mailConfig;

    @SneakyThrows
    @Override
    public void onApplicationEvent(CommentNotification event) {
        sendEmail(event.getMessage(), event.getSubject(), event.getPostOwner(), event.getCommenterUsername());
    }

    public void sendEmail(String message, String subject, User postOwner, String commenterUsername) throws MessagingException, UnsupportedEncodingException {
        String senderName = "Social_media_api";
        String mailContent = "<p> Hi, " + postOwner.getUsername() + "</p>" +
                "<p>User <strong>" + commenterUsername + "</strong> commented on your post:</p>" + "<p>" + message + "</p>" +
                "<p><br>" + senderName + "</p>";
        MimeMessage mimeMessage = mailConfig.javaMailSender().createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("Social_media_api", senderName);
        helper.setSubject(subject);
        helper.setTo(postOwner.getEmail());
        helper.setText(mailContent, true);
        mailConfig.javaMailSender().send(mimeMessage);
    }
}
