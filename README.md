# Issue Tracker API

A Spring Boot REST API for tracking issues and enabling team collaboration through issue comments.

## Features

- Create, update, assign, and close issues
- Add comments to issues for team collaboration
- Retrieve comments in chronological order
- Input validation and error handling
- RESTful API design

## Technology Stack

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Validation (Bean Validation / JSR-380)
- H2 In-Memory Database
- Maven

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

```bash
cd issue-tracker-api
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

### H2 Console

Access the H2 in-memory database console at:

```
http://localhost:8080/h2-console
```

JDBC URL: `jdbc:h2:mem:issuetracker`
Username: `sa`
Password: *(leave blank)*

## API Endpoints

### Issue Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/issues` | Retrieve all issues |
| GET | `/api/issues/{id}` | Retrieve a specific issue by ID |
| POST | `/api/issues` | Create a new issue |
| PUT | `/api/issues/{id}` | Update an existing issue |
| DELETE | `/api/issues/{id}` | Delete an issue |

### Comment Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/issues/{issueId}/comments` | Add a comment to an issue |
| GET | `/api/issues/{issueId}/comments` | Retrieve all comments for an issue (chronological order) |

## Comment Feature

The comments feature allows project team members to collaborate directly within an issue.

### Add a Comment

**POST** `/api/issues/{issueId}/comments`

**Request Body:**

```json
{
  "author": "Jane Doe",
  "commentText": "This issue has been reproduced on staging environment."
}
```

**Response (201 Created):**

```json
{
  "id": 1,
  "author": "Jane Doe",
  "commentText": "This issue has been reproduced on staging environment.",
  "createdAt": "2026-08-06T10:30:00",
  "issueId": 42
}
```

### Get Comments for an Issue

**GET** `/api/issues/{issueId}/comments`

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "author": "Jane Doe",
    "commentText": "This issue has been reproduced on staging environment.",
    "createdAt": "2026-08-06T10:30:00",
    "issueId": 42
  },
  {
    "id": 2,
    "author": "John Smith",
    "commentText": "Fix is in progress, targeting next sprint.",
    "createdAt": "2026-08-06T11:15:00",
    "issueId": 42
  }
]
```

### Validation Rules

- `author` must not be blank
- `commentText` must not be empty or blank
- Requests with empty comment text will receive a `400 Bad Request` response

### Backward Compatibility

All existing Issue API endpoints remain fully functional and unchanged. The comments feature is purely additive and does not introduce any breaking changes to existing clients.

## Error Handling

| HTTP Status | Scenario |
|-------------|----------|
| 201 Created | Comment successfully created |
| 200 OK | Comments successfully retrieved |
| 400 Bad Request | Validation failure (e.g., empty comment text or blank author) |
| 404 Not Found | Issue not found for the given issueId |

## Project Structure

```
issue-tracker-api/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/issuetracker/
│       │       ├── controller/
│       │       │   ├── IssueController.java
│       │       │   └── CommentController.java
│       │       ├── service/
│       │       │   ├── IssueService.java
│       │       │   └── CommentService.java
│       │       ├── repository/
│       │       │   ├── IssueRepository.java
│       │       │   └── CommentRepository.java
│       │       ├── entity/
│       │       │   ├── Issue.java
│       │       │   └── Comment.java
│       │       ├── dto/
│       │       │   ├── CommentRequestDTO.java
│       │       │   └── CommentResponseDTO.java
│       │       └── exception/
│       │           └── EmptyCommentException.java
│       └── resources/
│           └── application.properties
└── pom.xml
```