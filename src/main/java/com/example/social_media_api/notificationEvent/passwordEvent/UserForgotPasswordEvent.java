package com.example.social_media_api.notificationEvent.passwordEvent;

import com.example.social_media_api.entity.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.ApplicationEvent;
@Data
@Builder
public class UserForgotPasswordEvent extends ApplicationEvent {
    private User user;
    private  String applicationUrl;

    public UserForgotPasswordEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
