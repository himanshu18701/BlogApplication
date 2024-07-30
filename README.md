# Blog Application

## Overview
This project is a Blog Application designed using Spring Boot, Thymeleaf, and Spring Security. It demonstrates the integration of various Spring technologies to create a secure and user-friendly web application for blogging. The application supports user authentication, authorization, and CRUD operations on blog posts.

## Features
- User Registration and Login
- Role-based Access Control
- Create, Read, Update, and Delete (CRUD) operations on blog posts
- Secure endpoints with Spring Security
- Thymeleaf templates for server-side rendering
- Integration with a relational database

## Technologies Used
- **Spring Boot**: For building the RESTful application.
- **Spring Security**: For securing the application.
- **Thymeleaf**: For server-side rendering of HTML pages.
- **JPA/Hibernate**: For ORM (Object Relational Mapping) with the database.
- **H2 Database**: In-memory database for development and testing.
- **JWT (JSON Web Token)**: For securing API endpoints.

## Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- IDE (IntelliJ IDEA recommended)

## Getting Started

### Clone the Repository
```bash
git clone https://github.com/yourusername/blogapplication.git
cd blogapplication
```

## Configure Database

Update the `application.properties` file in `src/main/resources` to configure the database settings.

```properties
# Spring Datasource
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password

# Hibernate properties
spring.jpa.hibernate.ddl-auto=update
```

