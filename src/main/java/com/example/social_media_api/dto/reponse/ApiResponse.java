package com.example.social_media_api.dto.reponse;

import com.example.social_media_api.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private String time;
    private T data;
    public ApiResponse (T data){
        this.message = "processed Successfully";
        this.time = DateUtils.saveDate(LocalDateTime.now());
        this.data = data;
    }
}
