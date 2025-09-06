# 🎬 CinemaGo - Movie Reservation System

**CinemaGo** is a comprehensive movie ticket booking system built with **Spring Boot 3.2** and **Java 21**. It provides a robust backend with endpoints for user authentication, movie management, hall & seat reservations, secure payments via **Stripe**, and more.

The entire system is containerized with **Docker Compose** for easy setup and deployment. It can be exposed to the internet using **ngrok** for testing webhook integrations in a development environment.

---

## 📋 Table of Contents

- [🚀 Features](#-features)
- [🛠️ Tech Stack](#️-tech-stack)
- [✅ Prerequisites](#-prerequisites)
- [⚙️ Getting Started](#️-getting-started)
  - [1. Clone the Repository](#1-clone-the-repository)
  - [2. Configuration](#2-configuration)
  - [3. Build and Run with Docker Compose](#3-build-and-run-with-docker-compose)
  - [4. Access the Services](#4-access-the-services)
  - [5. Stop the Containers](#5-stop-the-containers)
- [🌍 Exposing with ngrok (for Webhooks)](#-exposing-with-ngrok-for-webhooks)
- [📖 API Documentation](#-api-documentation)
- [📝 Quick API Examples](#-quick-api-examples)
  - [User Registration](#user-registration)
  - [Fetch All Movies](#fetch-all-movies)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)
- [👤 Author](#-author)

---

## 🚀 Features

CinemaGo is a full-featured movie reservation system built with **Spring Boot**, providing a secure, scalable, and production-ready architecture. Below are the key features:

### 🔐 Authentication & Security
- **JWT Authentication**: Secure login and token-based session handling.
- **Role-Based Access Control**: Fine-grained permissions with `@PreAuthorize` method-level annotations (`Admin` / `User`).
- **Password Encryption**: Strong hashing for user credentials.
- **CORS Configurations**: Safe integration with external clients.

### 🎬 Movie & Showtime Management
- **Admin Panel**: Add, update, and delete movies, halls, and showtimes.
- **Crew Management**: Manage directors, actors, and production teams.
- **Hall Layouts**: Define seat layouts and capacity for each hall.

### 🎟️ Booking & Reservation
- **Real-Time Seat Reservation**: Prevents double-booking with concurrent access validation.
- **Capacity Validation**: Ensures reservations cannot exceed hall capacity.
- **User-Friendly Booking Flow**: Browse available showtimes and reserve instantly.

### 💳 Payment Gateway (Stripe)
- **Stripe Integration**: Secure online payments for tickets.
- **Webhook Support**: Automatic confirmation of successful transactions.

### 📧 Email Notifications
- **Gmail SMTP Integration**: Automated emails for:
  - Registration confirmation.
  - Password reset.

### 📖 API Documentation
- **Swagger / OpenAPI**: Interactive API docs at `/swagger-ui.html`.
- Full endpoint reference for authentication, movies, booking, and payments.

### 📝 Logging & Monitoring
- **Centralized Logging**: Tracks user actions, errors, and system events.
- **Spring Boot Logging** with structured logs.
- **Debugging & Monitoring** support for production environments.

### 🧪 Testing
- **Unit Testing**: Validates service and utility logic with **JUnit 5** & **Mockito**.  
- **Integration Testing**: End-to-end validation of APIs with **Spring Boot Test** & **MockMvc**.  
- **Database Testing**: Ensures repository reliability with **Spring Data JPA Test** and H2 in-memory database.  
- **Controller Layer Testing**: Secure and accurate validation of request/response handling.  

### ☁️ Cloud & Deployment
- **Docker Compose**: One command to start the database and application.
- **PostgreSQL Database**: Persistent storage with mounted volume.
- **Cloudinary Integration**: Manage and host movie posters & images.
- **Ngrok Support**: Expose local environment to the internet for testing (e.g., Stripe webhooks).
---

## 🛠️ Tech Stack

-   **Backend:** Java 21, Spring Boot 3.2, Spring Security, JWT
-   **Database:** PostgreSQL 15
-   **Payments:** Stripe API
-   **Containerization:** Docker & Docker Compose
-   **API Docs:** Springdoc OpenAPI (Swagger UI)
-   **Build Tool:** Maven

---

## ✅ Prerequisites

Before you begin, ensure you have the following installed on your system:
-   [Git](https://git-scm.com/)
-   [JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
-   [Apache Maven](https://maven.apache.org/download.cgi)
-   [Docker & Docker Compose](https://www.docker.com/products/docker-desktop/)
-   [ngrok](https://ngrok.com/download) (Optional, for webhook testing)

---

## ⚙️ Getting Started

Follow these steps to get the application running locally.

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/CinemaGo.git
cd CinemaGo
```

### 2. Configuration

The application uses **environment variables** to manage sensitive information such as database credentials, email settings, and Stripe API keys.  
These values are passed via **Docker Compose** and injected into the application using `application.properties` placeholders.

### Example: `application.properties`

```properties
# ===============================
# Database Configuration
# ===============================
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO}
spring.jpa.show-sql=${SPRING_JPA_SHOW_SQL}
spring.jpa.properties.hibernate.format_sql=${SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# ===============================
# Mail Configuration
# ===============================
spring.mail.host=${SPRING_MAIL_HOST}
spring.mail.port=${SPRING_MAIL_PORT}
spring.mail.username=${SPRING_MAIL_USERNAME}
spring.mail.password=${SPRING_MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=${SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH}
spring.mail.properties.mail.smtp.starttls.enable=${SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE}

# ===============================
# Stripe Configuration
# ===============================
stripe.api.key=${STRIPE_API_KEY}
stripe.public.key=${STRIPE_PUBLIC_KEY}
stripe.webhook.secret=${STRIPE_WEBHOOK_SECRET}

# ===============================
# Server Configuration
# ===============================
server.port=${SERVER_PORT}
spring.application.name=${SPRING_APPLICATION_NAME}
```
## 3. Build and Run with Docker Compose
First, build the Spring Boot application JAR file:

```bash
mvn clean package -DskipTests
```
Then, start all services using Docker Compose:

```bash
docker compose up -d --build
```
This command will build the images and start the Spring Boot application and PostgreSQL database containers in the background.

## 4. Access the Services
Once the containers are running, you can access the application:

- Backend API: ```http://localhost:2200```
- Swagger Docs: ```http://localhost:2200/swagger-ui.html```
## 5. Stop the Containers
To stop and remove the running containers, use:
```bash
docker compose down
```
- ## 🌍 Exposing with ngrok (for Webhooks)
Ngrok is a great tool for exposing your local server to the internet. This is particularly useful for testing services that require a public callback URL, like Stripe webhooks.
- 1. Start the ngrok tunnel pointing to your local application port:

```bash
ngrok http 2200
```
- 2. Copy the public URL provided by ngrok. It will look something like this:

```text
https://random-id.ngrok-free.app
```
Configure Webhooks: Use this public URL in your Stripe Dashboard to set up the webhook endpoint (e.g., ```https://random-id.ngrok-free.app/api/v1/stripe/webhook```).

## 📖 API Documentation
Full API documentation is available via Swagger UI. This interactive interface allows you to explore endpoints, view models, and test API calls directly from your browser.

- Swagger UI: ```http://localhost:2200/swagger-ui.html```
- OpenAPI JSON Spec: ```http://localhost:2200/v3/api-docs```

## 📝 Quick API Examples
Here are a few examples of how to interact with the API.

### User Registration
```POST /api/v1/auth/register```

#### Request Body:

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "Password123!"
}
```
#### Success Response (201 Created):

```json
{
  "message": "User registered successfully. Please check your email for verification link."
}
```

### Fetch All Movies
```GET /api/v1/movies```

#### Success Response (200 OK):

```json
[
  {
    "id": 1,
    "title": "Inception",
    "genre": "Sci-Fi",
    "director": "Christopher Nolan",
    "durationMinutes": 148,
    "releaseDate": "2010-07-16"
  },
  {
    "id": 2,
    "title": "The Matrix",
    "genre": "Sci-Fi",
    "director": "The Wachowskis",
    "durationMinutes": 136,
    "releaseDate": "1999-03-31"
  }
]
```

## 🤝 Contributing
Contributions are welcome! If you'd like to contribute to CinemaGo, please follow these steps:

1. Fork the repository.
2. Create a new branch (git checkout -b feature/your-feature-name).
3. Make your changes and commit them (git commit -m 'Add some feature').
4. Push to the branch (git push origin feature/your-feature-name).
5. Open a Pull Request.

## 📄 License
This project is licensed under the MIT License. See the LICENSE file for details.

## 👤 Author
Hassan Alwakeel

- LinkedIn: [linkedin.com/in/your-linkedin-profile](https://www.linkedin.com/in/hassan-alwakeel-617537287/) <br>
- GitHub: [github.com/your-github-username](https://github.com/HassanAlwakeel1)
