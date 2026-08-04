# Issue Tracker API

A Spring Boot REST API for tracking and managing issues, enabling teams to create, update, assign, close, and now comment on issues for improved collaboration.

## Features

- Create, update, assign, and close issues
- Add comments to issues for team collaboration
- Retrieve comments in chronological order
- Validation to prevent empty comments
- Backward-compatible Issue APIs

## Technology Stack

- Java
- Spring Boot
- Spring Data JPA
- Maven
- H2 (in-memory database for development)

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Build and Run

```bash
cd issue-tracker-api
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

## API Endpoints

### Issues

Refer to [issue-tracker-api/README.md](issue-tracker-api/README.md) for full API documentation.

### Comments

| Method | Endpoint                              | Description                                      |
|--------|---------------------------------------|--------------------------------------------------|
| POST   | `/api/issues/{issueId}/comments`      | Add a new comment to an issue                    |
| GET    | `/api/issues/{issueId}/comments`      | Retrieve all comments for an issue (chronological) |
| GET    | `/api/comments/{commentId}`           | Retrieve a specific comment by ID                |

## Documentation

Full API documentation is available in [issue-tracker-api/README.md](issue-tracker-api/README.md).