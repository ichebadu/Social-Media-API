package com.example.social_media_api.notificationEvent;

import com.example.social_media_api.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PostEmailNotification extends ApplicationEvent {

    private User postOwner;
    private String message;
    private String subject;
    private User user;

    public PostEmailNotification(User postOwner, String subject, String message, User user) {
        super(postOwner);
        this.postOwner = postOwner;
        this.message = message;
        this.subject = subject;
        this.user = user;
    }
}

