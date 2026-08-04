# Issue Tracker API — Developer Guide

This document provides technical documentation for the Issue Tracker API, including entity structures, API endpoint specifications, request/response formats, validation rules, and backward compatibility notes.

## Project Structure

```
issue-tracker-api/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/issuetracker/
│       │       ├── controller/
│       │       │   └── CommentController.java
│       │       ├── dto/
│       │       │   ├── CommentRequestDTO.java
│       │       │   └── CommentResponseDTO.java
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

### Overview

The comment feature enables project team members to collaborate directly within issues. Comments are associated with a single issue, captured with author, timestamp, and comment text, and displayed in chronological order.

### Comment Entity

| Field | Column | Type | Constraints |
|-------|--------|------|-------------|
| id | id | BIGINT | Primary Key, Auto-generated |
| text | text | TEXT | NOT NULL, non-empty |
| author | author | VARCHAR(255) | NOT NULL, non-empty |
| createdAt | created_at | TIMESTAMP | NOT NULL, set on persist |
| issueId | issue_id | BIGINT | NOT NULL, references Issue |

### Database Schema

```sql
CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    text TEXT NOT NULL,
    author VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    issue_id BIGINT NOT NULL
);

CREATE INDEX idx_comment_issue_id ON comments (issue_id);
CREATE INDEX idx_comment_created_at ON comments (created_at);
```

## API Endpoints

### POST /api/issues/{issueId}/comments

Creates a new comment on the specified issue.

**Path Parameters:**
- `issueId` (Long, required) — the ID of the issue

**Request Body:**
```json
{
  "text": "This is a comment on the issue.",
  "author": "john.doe"
}
```

**Validation:**
- `text` — required, must not be blank or empty
- `author` — required, must not be blank or empty

**Responses:**

| Status | Description |
|--------|-------------|
| 201 Created | Comment created successfully |
| 400 Bad Request | Validation failure (empty text or author) |
| 500 Internal Server Error | Unexpected server error |

**Response Body (201):**
```json
{
  "id": 1,
  "text": "This is a comment on the issue.",
  "author": "john.doe",
  "createdAt": "2024-01-15T10:30:00",
  "issueId": 42
}
```

---

### GET /api/issues/{issueId}/comments

Retrieves all comments for the specified issue in chronological order (ascending by `createdAt`).

**Path Parameters:**
- `issueId` (Long, required) — the ID of the issue

**Responses:**

| Status | Description |
|--------|-------------|
| 200 OK | List of comments returned (may be empty) |
| 500 Internal Server Error | Unexpected server error |

**Response Body (200):**
```json
[
  {
    "id": 1,
    "text": "First comment.",
    "author": "alice",
    "createdAt": "2024-01-15T08:00:00",
    "issueId": 42
  },
  {
    "id": 2,
    "text": "Second comment.",
    "author": "bob",
    "createdAt": "2024-01-15T09:30:00",
    "issueId": 42
  }
]
```

---

### GET /api/comments/{commentId}

Retrieves a specific comment by its unique ID.

**Path Parameters:**
- `commentId` (Long, required) — the ID of the comment

**Responses:**

| Status | Description |
|--------|-------------|
| 200 OK | Comment found and returned |
| 404 Not Found | No comment exists with the given ID |
| 500 Internal Server Error | Unexpected server error |

**Response Body (200):**
```json
{
  "id": 1,
  "text": "First comment.",
  "author": "alice",
  "createdAt": "2024-01-15T08:00:00",
  "issueId": 42
}
```

---

## Validation Rules

| Field | Rule | Error |
|-------|------|-------|
| text | Must not be null, blank, or empty | 400 Bad Request |
| author | Must not be null, blank, or empty | 400 Bad Request |

## Backward Compatibility

The introduction of the comment feature does not modify any existing Issue API endpoints. All existing Issue request and response structures remain unchanged. The `comments` table is a new addition to the database schema and does not alter existing tables.

## Chronological Ordering

Comments are always returned in ascending order of their `created_at` timestamp. This is enforced at the repository layer via `findByIssueIdOrderByCreatedAtAsc`.

## Error Handling

| Exception | HTTP Status | Description |
|-----------|-------------|-------------|
| EmptyCommentException | 400 Bad Request | Comment text is blank or empty |
| IllegalArgumentException | 400 Bad Request | Author is blank or empty |
| NoSuchElementException | 404 Not Found | Comment not found by ID |
| Exception | 500 Internal Server Error | Unexpected server error |

## Configuration

Key properties in `application.properties`:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:h2:mem:issuetracker
logging.level.com.issuetracker=INFO
```