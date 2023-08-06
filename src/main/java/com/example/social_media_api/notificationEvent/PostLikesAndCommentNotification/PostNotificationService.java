//package com.example.social_media_api.notificationEvent.PostLikesAndCommentNotification;
//
//import com.example.social_media_api.config.MailConfig;
//import com.example.social_media_api.entity.Post;
//import com.example.social_media_api.entity.User;
//import lombok.Data;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import org.springframework.context.ApplicationEvent;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.stereotype.Service;
//
//@Getter
//@Setter
//public class PostNotificationService  extends ApplicationEvent {
//    private User user;
//
//    private String subject;
//    private String message;
//
//    public PostNotificationService(Object object, String subject, String message) {
//        super(object);
//        this.user=user;
//        this.subject=subject;
//        this.message=message;
//    }
//}
