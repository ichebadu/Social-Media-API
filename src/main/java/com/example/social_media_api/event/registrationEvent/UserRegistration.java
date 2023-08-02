package com.example.social_media_api.event.registrationEvent;


import com.example.social_media_api.entity.User;
import lombok.*;
import org.springframework.context.ApplicationEvent;
@Setter
@Getter
public class UserRegistration extends ApplicationEvent{
    private User user;
    private String otp;

    public UserRegistration(User user, String otp) {
        super(user);
        this.user = user;
        this.otp = otp;
    }
}
