package com.example.social_media_api.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "spring boot Social Media Api",
                description = "A RestFUL web-service that provides crud operations for a social media api",
                version = "v.10",
                contact = @Contact(
                        name = "ichebadu",
                        email = "chukwu.iche@gmail.com",
                        url = "https://github.com/ichebadu"
                ),
                license = @License(
                        name = "Apache 2.1.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Spring Boot Social Media Api Documentation",
                url = "https://github.com/ichebadu"
        )
)
public class SwaggerConfig {

}
