FROM openjdk:17
EXPOSE 8011
COPY target/social_media_api-0.0.1-SNAPSHOT.jar /social_media_api.jar
ENTRYPOINT ["java", "-jar", "/social_media_api.jar"]