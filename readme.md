# Users CRUD Application

A RESTful CRUD application for user management built with Kotlin, Spring Boot, and Gradle.  
It uses a PostgreSQL database and supports authentication and authorization.

## Features

- User registration and login (JWT-based authentication)
- Role-based access control
- CRUD operations for users
- Audit logging with JSONB column
- SQL database migrations (Flyway)
- Spring security
- JWT authorization

## Technologies

- Kotlin & Java
- Spring Boot
- Gradle
- PostgreSQL
- Flyway (database migrations)

## Getting Started

### Prerequisites

- JDK 17+
- PostgreSQL
- Gradle

### Setup

1. Clone the repository:
    ```
    git clone https://github.com/Daniel-Capla/users-crud.git
    cd users-crud
    ```

2. Start the application and PostgreSQL database using Docker Compose:
    ```
    docker-compose up --build
    ```

This will automatically configure and start both the backend and the database.

3. (Optional) Run database migrations manually:
    ```
    ./gradlew flywayMigrate
    ```

4. Access the application as described in the API Endpoints section.

## API Endpoints

- `GET /users` — List users (requires authentication)
- `GET /user?{id}` — Create user (requires authentication)
- `POST /register` — Register a new user
- `PUT /update?{id}{password}{username}` — Update user (requires authentication)
- `DELETE /delete?{id}` — Delete user for USER access (requires authentication, soft delete)
- `DELETE /admin-delete?{id}` — Delete user for ADMIN access (requires authentication, hard delete)
- `POST /login` — Authenticate and receive JWT

## Security

- JWT authentication
- Role-based access with `@PreAuthorize`

## Database Migration

Flyway migration scripts are in `src/main/resources/db/migration/`.