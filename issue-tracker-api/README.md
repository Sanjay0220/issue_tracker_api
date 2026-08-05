# Issue Tracker API - Module Documentation

This module contains the Spring Boot application for the Issue Tracker API.

## Module Structure

```
issue-tracker-api/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/issuetracker/
│       │       ├── controller/
│       │       │   └── CommentController.java
│       │       ├── dto/
│       │       │   ├── CommentDTO.java
│       │       │   └── CreateCommentRequest.java
│       │       ├── entity/
│       │       │   └── Comment.java
│       │       ├── exception/
│       │       │   └── EmptyCommentException.java
│       │       ├── repository/
│       │       │   └── CommentRepository.java
│       │       └── service/
│       │           └── CommentService.java
│       └── resources/
│           └── application.properties
└── pom.xml
```

## Comment Feature

The comment feature enables team members to collaborate directly within an issue by adding timestamped comments.

### Data Model

The `Comment` entity contains the following fields:

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Auto-generated primary key |
| issueId | Long | Foreign key referencing the associated issue |
| author | String | Name of the comment author |
| commentText | String | Text content of the comment |
| createdAt | LocalDateTime | Timestamp when the comment was created |

### Database Schema

The following table and indexes are created automatically by JPA/Hibernate:

```sql
CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    issue_id BIGINT NOT NULL,
    author VARCHAR(255) NOT NULL,
    comment_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_comment_issue_id ON comments (issue_id);
CREATE INDEX idx_comment_created_at ON comments (created_at);
```

### API Endpoints

#### POST /api/issues/{issueId}/comments

Creates a new comment for the specified issue.

**Path Parameters:**
- `issueId` (Long, required) - The ID of the issue to comment on

**Request Body:**
```json
{
  "author": "Jane Doe",
  "commentText": "Investigation complete. Root cause identified."
}
```

**Success Response (201 Created):**
```json
{
  "id": 1,
  "issueId": 10,
  "author": "Jane Doe",
  "commentText": "Investigation complete. Root cause identified.",
  "createdAt": "2026-08-05T14:00:00"
}
```

**Error Responses:**
- `400 Bad Request` - When `author` or `commentText` is blank or missing

#### GET /api/issues/{issueId}/comments

Retrieves all comments for the specified issue in chronological order (oldest first).

**Path Parameters:**
- `issueId` (Long, required) - The ID of the issue

**Success Response (200 OK):**
```json
[
  {
    "id": 1,
    "issueId": 10,
    "author": "Jane Doe",
    "commentText": "Investigation complete. Root cause identified.",
    "createdAt": "2026-08-05T14:00:00"
  },
  {
    "id": 2,
    "issueId": 10,
    "author": "John Smith",
    "commentText": "Fix deployed to staging.",
    "createdAt": "2026-08-05T15:30:00"
  }
]
```

Returns an empty array `[]` if no comments exist for the issue.

#### GET /api/comments/{commentId}

Retrieves a specific comment by its unique ID.

**Path Parameters:**
- `commentId` (Long, required) - The ID of the comment

**Success Response (200 OK):**
```json
{
  "id": 1,
  "issueId": 10,
  "author": "Jane Doe",
  "commentText": "Investigation complete. Root cause identified.",
  "createdAt": "2026-08-05T14:00:00"
}
```

**Error Responses:**
- `404 Not Found` - When no comment exists with the given ID

### Validation Rules

| Field | Rule | Error |
|-------|------|-------|
| author | Must not be blank | 400 Bad Request |
| commentText | Must not be blank | 400 Bad Request |

### Backward Compatibility

The comment feature is implemented as a purely additive extension. No existing Issue API endpoints have been modified. All existing clients continue to function without any changes.

### Architecture

The comment feature follows the standard layered architecture:

- **Controller** (`CommentController`) - Handles HTTP requests and responses
- **Service** (`CommentService`) - Contains business logic and validation
- **Repository** (`CommentRepository`) - Handles data persistence via Spring Data JPA
- **Entity** (`Comment`) - JPA entity mapped to the `comments` database table
- **DTOs** (`CommentDTO`, `CreateCommentRequest`) - Data transfer objects for API contracts
- **Exception** (`EmptyCommentException`) - Domain-specific exception for empty comment validation

## Configuration

Key configuration properties in `application.properties`:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:h2:mem:issuetracker
spring.jpa.show-sql=true
```

For production deployments, replace the H2 in-memory datasource with a persistent database (e.g., PostgreSQL, MySQL) and set `spring.jpa.hibernate.ddl-auto=validate` or use a migration tool such as Flyway or Liquibase.