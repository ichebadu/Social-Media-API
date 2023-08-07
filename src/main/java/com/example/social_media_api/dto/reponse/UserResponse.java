package com.example.social_media_api.dto.reponse;

import com.example.social_media_api.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String username;

    private String email;

    private Boolean status;

    private String imageLinkUrl;

    private Role role;
}
