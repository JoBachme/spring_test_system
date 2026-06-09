# Spring Test System

A small Spring Boot backend for managing students, tests, graded attempts, passing status, and simple result reports.

This project started as an interview practice exercise. It has been cleaned up as a compact learning project for modern Spring Boot, JPA, validation, profile-based configuration, and controller tests.

## Tech Stack

- Java 21
- Spring Boot 3.5.14
- Spring Web
- Spring Data JPA
- Bean Validation
- Flyway
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

Flyway applies the database schema and sample data from `src/main/resources/db/migration` when the application starts.
Existing local databases from the earlier SQL-init setup are baselined so they can keep their current data.

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

The suite includes controller slice tests and integration tests for the graded attempt rules.

## Sample Endpoints

Students:

```http
GET /api/v1/students?page=0&size=20&sort=lastName,asc&lastName=Smith
GET /api/v1/students/list
GET /api/v1/students/1
POST /api/v1/students
PUT /api/v1/students/1?firstName=Ada&lastName=Lovelace
DELETE /api/v1/students/1
```

Tests:

```http
GET /api/v1/tests?page=0&size=20&sort=testName,asc&testName=java&graded=true
GET /api/v1/tests/list
GET /api/v1/tests/1
POST /api/v1/tests
PUT /api/v1/tests/1?testName=Distributed Systems
PUT /api/v1/tests/1/graded-status
DELETE /api/v1/tests/1
```

Student test registrations and reports:

```http
POST /api/v1/registrations
DELETE /api/v1/registrations/1/3
GET /api/v1/student-tests
GET /api/v1/student-tests/1/3
GET /api/v1/student-tests/by-student/1
GET /api/v1/student-tests/by-test/3
GET /api/v1/student-tests/1/3/passed
PUT /api/v1/student-tests/1/3/attempts
PUT /api/v1/student-tests/1/3/passing-status
GET /api/v1/student-tests/failed
```

Message template:

```http
GET /api/v1/message-templates/notification_sample
PUT /api/v1/message-templates/notification_sample
```

## Error Responses

The API returns RFC 9457-style problem details for validation and business errors:

```json
{
  "title": "Validation failed",
  "status": 400,
  "detail": "Request validation failed",
  "path": "/api/v1/students",
  "errors": [
    {
      "field": "firstName",
      "message": "must not be blank"
    }
  ]
}
```

## Practice Roadmap

Good next exercises for this project:

- Add a simple notification preview endpoint that renders a message from the template.
- Add GitHub Actions for `./mvnw clean test`.
