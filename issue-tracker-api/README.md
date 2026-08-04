# Issue Tracker API - Developer Documentation

This document provides detailed API documentation for the Issue Tracker REST API, including the new Issue Comments feature.

## Base URL

```
http://localhost:8080/api
```

## Issue Comments Feature

The Issue Comments feature allows project team members to collaborate, discuss progress, and provide updates directly within an issue. Comments are associated with a single issue, displayed in chronological order, and require a non-empty comment text and author.

---

## Comment Endpoints

### POST /api/issues/{issueId}/comments

Adds a new comment to the specified issue.

**Path Parameters**

| Parameter | Type | Required | Description              |
|-----------|------|----------|--------------------------||
| issueId   | Long | Yes      | The ID of the issue      |

**Request Body**

```json
{
  "text": "This is a comment on the issue.",
  "author": "Jane Doe"
}
```

| Field  | Type   | Required | Validation              | Description              |
|--------|--------|----------|-------------------------|--------------------------||
| text   | String | Yes      | Must not be blank       | The comment text         |
| author | String | Yes      | Must not be blank       | The author of the comment|

**Response - 201 Created**

```json
{
  "id": 1,
  "text": "This is a comment on the issue.",
  "author": "Jane Doe",
  "createdAt": "2026-08-04T10:30:00",
  "issueId": 42
}
```

**Response - 400 Bad Request**

Returned when the comment text or author is empty or blank.

```json
"Comment text must not be empty"
```

**Response - 500 Internal Server Error**

Returned when an unexpected server error occurs.

---

### GET /api/issues/{issueId}/comments

Retrieves all comments for the specified issue in chronological order (oldest first).

**Path Parameters**

| Parameter | Type | Required | Description              |
|-----------|------|----------|--------------------------||
| issueId   | Long | Yes      | The ID of the issue      |

**Response - 200 OK**

```json
[
  {
    "id": 1,
    "text": "First comment on this issue.",
    "author": "Alice",
    "createdAt": "2026-08-04T09:00:00",
    "issueId": 42
  },
  {
    "id": 2,
    "text": "Second comment with an update.",
    "author": "Bob",
    "createdAt": "2026-08-04T10:30:00",
    "issueId": 42
  }
]
```

Returns an empty array `[]` if no comments exist for the issue.

**Response - 500 Internal Server Error**

Returned when an unexpected server error occurs.

---

### GET /api/comments/{commentId}

Retrieves a specific comment by its unique ID.

**Path Parameters**

| Parameter | Type | Required | Description              |
|-----------|------|----------|--------------------------||
| commentId | Long | Yes      | The ID of the comment    |

**Response - 200 OK**

```json
{
  "id": 1,
  "text": "This is a comment on the issue.",
  "author": "Jane Doe",
  "createdAt": "2026-08-04T10:30:00",
  "issueId": 42
}
```

**Response - 404 Not Found**

Returned when no comment exists with the given ID.

```json
"Comment not found with ID: 1"
```

**Response - 500 Internal Server Error**

Returned when an unexpected server error occurs.

---

## Comment Data Model

| Field     | Type          | Description                                      |
|-----------|---------------|--------------------------------------------------|
| id        | Long          | Unique identifier for the comment (auto-generated) |
| text      | String        | The content of the comment (must not be empty)   |
| author    | String        | The name of the person who wrote the comment     |
| createdAt | LocalDateTime | The timestamp when the comment was created (UTC) |
| issueId   | Long          | The ID of the issue this comment belongs to      |

---

## Validation Rules

- `text` must not be null, empty, or blank.
- `author` must not be null, empty, or blank.
- A comment must be associated with a valid `issueId`.

---

## Ordering

Comments are always returned in **chronological order** (ascending by `createdAt` timestamp) when retrieved via `GET /api/issues/{issueId}/comments`.

---

## Backward Compatibility

All existing Issue API endpoints remain fully backward compatible. No changes have been made to existing Issue request/response contracts.

---

## Database Schema

```sql
CREATE TABLE comments (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    text       TEXT          NOT NULL,
    author     VARCHAR(255)  NOT NULL,
    created_at TIMESTAMP     NOT NULL,
    issue_id   BIGINT        NOT NULL
);

CREATE INDEX idx_comment_issue_id  ON comments (issue_id);
CREATE INDEX idx_comment_created_at ON comments (created_at);
```