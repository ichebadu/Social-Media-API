package com.example.social_media_api.notificationEvent.registrationEvent;


import com.example.social_media_api.entity.User;
import lombok.*;
import org.springframework.context.ApplicationEvent;
@Setter
@Getter
public class UserRegistrationEvent extends ApplicationEvent{
    private User user;
    private String otp;

    public UserRegistrationEvent(User user, String otp) {
        super(user);
        this.user = user;
        this.otp = otp;
    }
}
