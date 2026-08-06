# Issue Tracker API - Developer Guide

## Overview

The Issue Tracker API is a Spring Boot REST API that enables teams to create, manage, and collaborate on issues. The comments feature allows team members to communicate directly within an issue, capturing author, timestamp, and comment text for full traceability.

## Comment API Reference

### Base URL

```
http://localhost:8080
```

---

### POST /api/issues/{issueId}/comments

Adds a new comment to an existing issue.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| issueId | Long | Yes | The unique identifier of the issue |

**Request Headers:**

| Header | Value |
|--------|-------|
| Content-Type | application/json |

**Request Body:**

```json
{
  "author": "string (required, not blank)",
  "commentText": "string (required, not empty)"
}
```

**Field Constraints:**

| Field | Constraint |
|-------|------------|
| author | Must not be blank |
| commentText | Must not be empty or blank |

**Success Response - 201 Created:**

```json
{
  "id": 1,
  "author": "Jane Doe",
  "commentText": "This issue has been reproduced on staging.",
  "createdAt": "2026-08-06T10:30:00",
  "issueId": 42
}
```

**Error Responses:**

| Status | Reason |
|--------|--------|
| 400 Bad Request | commentText is empty/blank or author is blank |
| 404 Not Found | Issue with specified issueId does not exist |

**Example cURL:**

```bash
curl -X POST http://localhost:8080/api/issues/42/comments \
  -H "Content-Type: application/json" \
  -d '{"author": "Jane Doe", "commentText": "Reproduced on staging."}'
```

---

### GET /api/issues/{issueId}/comments

Retrieves all comments for the specified issue in chronological order (oldest first).

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| issueId | Long | Yes | The unique identifier of the issue |

**Success Response - 200 OK:**

```json
[
  {
    "id": 1,
    "author": "Jane Doe",
    "commentText": "This issue has been reproduced on staging.",
    "createdAt": "2026-08-06T10:30:00",
    "issueId": 42
  },
  {
    "id": 2,
    "author": "John Smith",
    "commentText": "Fix deployed to staging for verification.",
    "createdAt": "2026-08-06T14:00:00",
    "issueId": 42
  }
]
```

Returns an empty array `[]` if the issue has no comments.

**Error Responses:**

| Status | Reason |
|--------|--------|
| 404 Not Found | Issue with specified issueId does not exist |

**Example cURL:**

```bash
curl -X GET http://localhost:8080/api/issues/42/comments
```

---

## Data Model

### Comment

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Auto-generated unique identifier |
| author | String | The author of the comment |
| commentText | String | The text content of the comment |
| createdAt | LocalDateTime | Timestamp when the comment was created (auto-set) |
| issueId | Long | The ID of the issue this comment belongs to |

---

## Validation

- `author` must not be blank. Requests with a blank author will receive `400 Bad Request`.
- `commentText` must not be empty or blank. Requests with empty comment text will receive `400 Bad Request`.
- Comments are automatically timestamped at creation time by the server.

---

## Backward Compatibility

All existing Issue API endpoints remain fully functional and unchanged. The comments feature is purely additive. No existing Issue API contracts have been modified.

---

## Database Schema

The `comments` table is automatically created by Hibernate based on the `Comment` entity definition.

```sql
CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_text TEXT NOT NULL,
    author VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    issue_id BIGINT NOT NULL,
    CONSTRAINT fk_comment_issue FOREIGN KEY (issue_id) REFERENCES issues(id)
);

CREATE INDEX idx_comment_issue_id ON comments (issue_id);
CREATE INDEX idx_comment_created_at ON comments (created_at);
```

---

## Running Locally

```bash
mvn spring-boot:run
```

Application starts on `http://localhost:8080`.

H2 Console available at `http://localhost:8080/h2-console`.