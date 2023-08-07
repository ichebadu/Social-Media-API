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
public class PostEventListener implements ApplicationListener<PostEmailNotification> {

    private final MailConfig mailConfig;

    @SneakyThrows
    @Override
    public void onApplicationEvent(PostEmailNotification event) {
        sendEmail(event.getMessage(), event.getSubject(), event.getPostOwner(), event.getUser());
    }

    public void sendEmail(String message, String subject, User postOwner, User commenterUsername) throws MessagingException, UnsupportedEncodingException {
        String senderName = "Social_media_api";
        String mailContent = buildMailContent(commenterUsername.getEmail(), message, senderName);

        MimeMessage mimeMessage = mailConfig.javaMailSender().createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("Social_media_api", senderName);
        helper.setSubject(subject);
        helper.setTo(postOwner.getEmail());
        helper.setText(mailContent, true);
        mailConfig.javaMailSender().send(mimeMessage);
    }

    private String buildMailContent(String postOwnerUsername, String message, String senderName) {
        return new StringBuilder()
                .append("<p> Hi, ").append(postOwnerUsername).append("</p>")
                .append("<p>").append(message).append("</p>")
                .append("<p><br>").append(senderName).append("</p>")
                .toString();
    }
}
