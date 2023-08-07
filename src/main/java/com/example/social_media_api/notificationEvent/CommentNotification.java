package com.example.social_media_api.notificationEvent;

import com.example.social_media_api.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CommentNotification extends ApplicationEvent {
    private User postOwner;
    private String message;
    private String subject;
    private String commenterUsername;

    public CommentNotification(User postOwner, String message, String subject, String commenterUsername) {
        super(postOwner);
        this.postOwner = postOwner;
        this.message = message;
        this.subject = subject;
        this.commenterUsername = commenterUsername;
    }
}
