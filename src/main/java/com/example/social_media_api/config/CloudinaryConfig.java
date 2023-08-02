package com.example.social_media_api.config;


import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Slf4j
public class CloudinaryConfig {
    private static final String CLOUD_NAME = "dezb6qbwe";
    private static final String CLOUD_KEY = "583534981816193";
    private static final String CLOUD_SECRET = "L8gMBYLMnYanoaWKaa7U9VzcUPA";

    public String imageLink(MultipartFile file, String id){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("cloud_key", CLOUD_KEY);
        config.put("cloud_secret", CLOUD_SECRET);
        Cloudinary cloudinary = new Cloudinary(config);
        try{
            cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("public", "image_id" +id));
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        String url = cloudinary.url().transformation(new Transformation().width(200).height(250).crop("fill")).generate("image_id"+id);
        log.info("image_url" + url);
        return url;
    }
}
