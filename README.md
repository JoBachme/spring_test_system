# Spring Test System

A small Spring Boot backend for managing students, tests, graded attempts, passing status, and simple result reports.

This project started as an interview practice exercise. It has been cleaned up as a compact learning project for modern Spring Boot, JPA, validation, profile-based configuration, and controller tests.

## Tech Stack

- Java 21
- Spring Boot 3.5.14
- Spring Web
- Spring Data JPA
- Bean Validation
- MySQL for local development
- H2 for automated tests
- Maven Wrapper

## Domain

The core model is a many-to-many relation:

```text
Student n:m Test
```

The join table `students_tests` stores test-specific state:

- whether a student passed a test
- how many attempts the student used
- whether the test is currently treated as graded

## Run Locally

Start MySQL:

```bash
docker compose up -d
```

Run the application:

```bash
./mvnw spring-boot:run
```

The API runs on:

```text
http://localhost:8080
```

OpenAPI documentation:

```text
http://localhost:8080/swagger-ui.html
http://localhost:8080/v3/api-docs
```

The default development database credentials are intentionally non-secret and match `docker-compose.yml`. For a custom database, copy `.env.example` to `.env` or configure these environment variables in your shell or IDE:

```bash
export DB_URL='jdbc:mysql://localhost:3306/spring_test_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
export DB_USERNAME='spring_test_user'
export DB_PASSWORD='spring_test_password'
```

## Test

```bash
./mvnw clean test
```

Tests use the `test` profile and an in-memory H2 database, so MySQL is not required for the test suite.

## Sample Endpoints

Students:

```http
GET /api/v1/student/all
GET /api/v1/student/id?studentId=1
POST /api/v1/student
PUT /api/v1/student/1?firstName=Ada&lastName=Lovelace
DELETE /api/v1/student/1
```

Tests:

```http
GET /api/v1/test/all
GET /api/v1/test/id?testId=1
POST /api/v1/test
PUT /api/v1/test/1?testName=Distributed Systems
PUT /api/v1/test/graded-status?testId=1
DELETE /api/v1/test/1
```

Student test registrations and reports:

```http
GET /api/v1/st_combination/all
GET /api/v1/st_combination/id?studentId=1&testId=3
GET /api/v1/st_combination/tests?studentId=1
GET /api/v1/st_combination/students?testId=3
GET /api/v1/st_combination/hasPassed?studentId=1&testId=3
PUT /api/v1/st_combination/addTry?studentId=1&testId=3
PUT /api/v1/st_combination/passing?studentId=1&testId=3
GET /api/v1/st_combination/failed
```

Message template:

```http
GET /api/v1/messagetemplate/notification_sample
PUT /api/v1/messagetemplate/notification_sample
```

## Practice Roadmap

Good next exercises for this project:

- Replace entity request bodies with DTOs and mappers.
- Add OpenAPI/Swagger documentation.
- Add integration tests for the graded attempt rules.
- Add endpoints for registering and unregistering students for tests.
- Add Flyway migrations instead of raw SQL init files.
- Add a simple notification preview endpoint that renders a message from the template.
- Add GitHub Actions for `./mvnw clean test`.
