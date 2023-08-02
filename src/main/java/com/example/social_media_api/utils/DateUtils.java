package com.example.social_media_api.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.swing.text.DateFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
public class DateUtils {

    public static String saveDate(LocalDateTime date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm:ss a" );
        return date.format(dateTimeFormatter);
    }
}
