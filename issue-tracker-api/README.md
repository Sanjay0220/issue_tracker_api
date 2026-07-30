# Issue Tracker API

A REST API for tracking software issues across projects, built with **Spring Boot 3**, **Spring Security 6**, **Spring Data JPA**, and **JWT** authentication.

## Features

- User registration & login with JWT-based authentication
- BCrypt password hashing
- Project CRUD (create, list, get, update, delete)
- Issue CRUD with status (`OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`) and priority (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`)
- Filter issues by project or assignee
- Centralized error handling with consistent JSON error responses
- Bean validation on all incoming request bodies
- H2 in-memory database out of the box (no setup required); easy to swap for MySQL/PostgreSQL
- CORS enabled for all origins (adjust for production)

## Tech Stack

| Layer          | Technology                          |
|----------------|--------------------------------------|
| Language       | Java 17                              |
| Framework      | Spring Boot 3.3.4                    |
| Security       | Spring Security 6 + JWT (jjwt 0.11.5)|
| Persistence    | Spring Data JPA / Hibernate          |
| Database       | H2 (in-memory, dev/test)             |
| Build tool     | Maven                                |
| Boilerplate    | Lombok                               |

## Project Structure

```
issue-tracker-api/
├── pom.xml
├── src/main/java/com/company/issuetracker/
│   ├── IssueTrackerApplication.java   # entry point
│   ├── controller/                    # REST endpoints
│   ├── service/                       # business logic
│   ├── repository/                    # Spring Data JPA repositories
│   ├── entity/                        # JPA entities (User, Project, Issue)
│   ├── dto/                           # request/response payloads
│   ├── config/                        # SecurityConfig
│   ├── security/                      # JwtUtil, JwtAuthenticationFilter, CustomUserDetailsService
│   └── exception/                     # ResourceNotFoundException, GlobalExceptionHandler
└── src/main/resources/application.properties
```

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Run locally

```bash
mvn spring-boot:run
```

The API will start on **http://localhost:8080**.

The H2 console is available at `http://localhost:8080/h2-console`
(JDBC URL: `jdbc:h2:mem:issuetrackerdb`, user: `sa`, password: empty).

### Build a jar

```bash
mvn clean package
java -jar target/issue-tracker-api-0.0.1-SNAPSHOT.jar
```

## Configuration

Key properties in `application.properties` (all overridable via environment variables):

| Property         | Env var           | Default                          |
|------------------|--------------------|-----------------------------------|
| `jwt.secret`     | `JWT_SECRET`       | pre-generated Base64 dev secret   |
| `jwt.expiration` | `JWT_EXPIRATION`   | `86400000` (24 hours, in ms)      |
| `server.port`    | `SERVER_PORT`      | `8080`                             |

> ⚠️ Replace `jwt.secret` with a securely generated, private value before deploying to production.

## API Reference

All endpoints are prefixed with `/api`. Endpoints under `/api/auth/**` are public; everything else requires a
`Authorization: Bearer <token>` header obtained from `/api/auth/login`.

### Auth

| Method | Endpoint             | Description            |
|--------|-----------------------|-------------------------|
| POST   | `/api/auth/register`  | Register a new user     |
| POST   | `/api/auth/login`     | Log in, returns JWT      |

**Register**
```json
POST /api/auth/register
{
  "username": "jdoe",
  "email": "jdoe@example.com",
  "password": "secret123"
}
```

**Login**
```json
POST /api/auth/login
{
  "username": "jdoe",
  "password": "secret123"
}
```
Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "userId": 1,
  "username": "jdoe",
  "role": "USER"
}
```

### Projects

| Method | Endpoint              | Description          |
|--------|------------------------|-----------------------|
| POST   | `/api/projects`        | Create a project       |
| GET    | `/api/projects`        | List all projects      |
| GET    | `/api/projects/{id}`   | Get a project by id    |
| PUT    | `/api/projects/{id}`   | Update a project       |
| DELETE | `/api/projects/{id}`   | Delete a project       |

```json
POST /api/projects
{
  "name": "Website Redesign",
  "description": "Revamp the marketing site"
}
```

### Issues

| Method | Endpoint                          | Description                    |
|--------|------------------------------------|----------------------------------|
| POST   | `/api/issues`                      | Create an issue                  |
| GET    | `/api/issues`                      | List all issues                  |
| GET    | `/api/issues/{id}`                 | Get an issue by id                |
| GET    | `/api/issues/project/{projectId}`  | List issues for a project         |
| GET    | `/api/issues/assignee/{assigneeId}`| List issues assigned to a user    |
| PUT    | `/api/issues/{id}`                 | Update an issue                  |
| DELETE | `/api/issues/{id}`                 | Delete an issue                  |

```json
POST /api/issues
{
  "title": "Login page throws 500",
  "description": "Stack trace attached in ticket #123",
  "status": "OPEN",
  "priority": "HIGH",
  "projectId": 1,
  "assigneeId": 2
}
```

## Error Response Format

```json
{
  "timestamp": "2026-07-30T10:15:30",
  "status": 404,
  "error": "Not Found",
  "message": "Project not found with id: 99"
}
```

Validation errors additionally include an `errors` map of field -> message.

## Notes / Next Steps

- Swap H2 for MySQL/PostgreSQL by updating `spring.datasource.*` properties and adding the relevant driver dependency to `pom.xml`.
- Add role-based method security (`@PreAuthorize`) if you need to restrict certain endpoints (e.g. only `ADMIN` can delete projects).
- Add pagination (`Pageable`) to the list endpoints for large datasets.
- Add refresh tokens if longer-lived sessions are needed.
