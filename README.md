# Social-Media-API
this social media api offers a scalable and secure backend infrastructure for creating engaging social media applications.
Sure! Below is the entire README for the Social Media API project:

# Social Media API

## Overview
The Social Media API is a Spring Boot application that provides a RESTful API for managing user accounts, posts, and comments in a social media platform. It allows users to create accounts, create and manage posts, follow other users, like and comment on posts, and more.

## Features
The Social Media API offers the following features:

1. User Management:
    - Register a new user account.
    - Authenticate users using JWT token-based authentication.
    - Update user profile information, including username, email, and profile picture.
    - Follow or unfollow other users.

2. Post Management:
    - Create new posts with content and an optional image.
    - Get a specific post by ID.
    - Get a list of all posts with pagination and sorting options.
    - Update post content and image.
    - Delete a post.

3. Comment Management:
    - Create comments on posts.
    - Get a specific comment by ID.
    - Get all comments for a specific post with pagination and sorting options.
    - Update and delete comments.

## API Documentation
The API is documented using OpenAPI (Swagger) and can be accessed through the following endpoint when running the application locally:
```
http://localhost:8010/swagger-ui.html
```
The Swagger documentation provides details on all available endpoints, request payloads, and response formats.

## Installation and Setup
To set up and run the Social Media API, follow these steps:

1. Clone the repository from GitHub.

2. Open the project in your preferred Java IDE (e.g., IntelliJ, Eclipse).

3. Configure the application.properties file with your database settings and email configuration.

4. Ensure you have PostgreSQL installed and create a database named "socialmediaapi".

5. Run the application as a Spring Boot application.

6. The API will be accessible at `http://localhost:8010`.

## Database
The Social Media API uses PostgreSQL as its database. The database schema is automatically created and updated based on the JPA entity models defined in the application.

## Dependencies
The Social Media API uses several libraries and dependencies, including:

- Spring Boot Starter Data JPA: For database access and ORM.
- Spring Boot Starter Security: For user authentication and authorization.
- Spring Boot Starter Mail: For sending email notifications.
- Spring Boot Starter Web: For building RESTful APIs.
- Jakarta Validation API: For data validation.
- ModelMapper: For object mapping.
- jjwt: For JSON Web Token (JWT) authentication.
- Spring Boot DevTools: For development convenience.
- Lombok: For reducing boilerplate code.
- PostgreSQL: As the relational database.
- Cloudinary: For image upload and management.

## Testing
The Social Media API includes comprehensive unit tests to ensure the correctness and reliability of the API endpoints. The tests cover various scenarios, including positive and negative test cases.

## Challenges
During the development of the Social Media API, I encountered several challenges that required innovative solutions to ensure the successful implementation of the project. Here are some of the key challenges I faced and how I overcame them:

1. JWT Token Authentication: Implementing secure JWT token-based authentication was a significant challenge. I needed to ensure that user authentication and authorization were properly handled, and tokens were validated to prevent unauthorized access to sensitive resources. To overcome this challenge, I extensively studied Spring Security and JWT best practices, which enabled me to design a robust and secure authentication system.

2. User Following System: Creating a system for users to follow and unfollow each other required careful design and consideration of the database schema. The challenge was to efficiently manage the relationships between users and their followers/following lists. I addressed this challenge by modeling the user relationships using one-to-many and many-to-many associations in the database, allowing for seamless handling of user interactions.

3. Image Upload and Management: Implementing image upload and management for user profile pictures and post images involved dealing with file storage and optimization. I opted to use Cloudinary, which provided a reliable cloud-based solution for image management. This decision allowed me to handle image uploads efficiently and serve them seamlessly in the API responses.

4. Unit Testing: Ensuring the correctness and reliability of the API endpoints through comprehensive unit testing was crucial. I faced challenges in setting up the testing environment and writing effective test cases. By investing time in learning testing frameworks and leveraging Spring Boot's testing support, I was able to create a robust test suite that helped identify and fix potential issues early in the development process.

5. Pagination and Sorting: Implementing pagination and sorting for listing posts and comments efficiently was another challenge. I had to design flexible and user-friendly APIs that allowed clients to request specific subsets of data. By leveraging Spring Data JPA's built-in support for pagination and sorting, I successfully implemented these features, enhancing the user experience.

6. Deployment and Dockerization: Preparing the application for deployment and dockerizing it for easy deployment in different environments presented some challenges. I had to ensure that all dependencies and configurations were correctly set up to run the application in a containerized environment. Through research and testing, I successfully created a Docker image that encapsulated the application and made it easy to deploy.

Overall