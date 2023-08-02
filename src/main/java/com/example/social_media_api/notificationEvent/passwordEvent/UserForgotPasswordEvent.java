package com.example.social_media_api.notificationEvent.passwordEvent;

import com.example.social_media_api.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
public class UserForgotPasswordEvent extends ApplicationEvent {
    private final User user;
    private final String resetToken;

    public UserForgotPasswordEvent(User user, String resetToken) {
        super(user);
        this.user = user;
        this.resetToken = resetToken;
    }

    public User getUser() {
        return user;
    }

    public String getResetToken() {
        return resetToken;
    }
}
