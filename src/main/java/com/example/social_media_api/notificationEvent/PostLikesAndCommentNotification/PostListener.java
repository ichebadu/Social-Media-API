//package com.example.social_media_api.notificationEvent.PostLikesAndCommentNotification;
//
//import com.example.social_media_api.config.MailConfig;
//import com.example.social_media_api.entity.Post;
//import com.example.social_media_api.entity.User;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import org.springframework.context.ApplicationListener;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class PostListener implements ApplicationListener<PostNotificationService> {
//    private final MailConfig mailConfig;
//
//    @SneakyThrows
//    public void SendEmail(String user, String subject, String messages) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(user);
//        message.setSubject("subject");
//        message.setText("body");
//        mailConfig.javaMailSender().send(message);
//    }
//    @Override
//    public void onApplicationEvent(PostNotificationService event) {
//        SendEmail(event.getUser().getEmail(),event.getSubject(),"");
//    }
//}
