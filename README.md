# Issue Tracker API

A RESTful Issue Tracker API built with Java and Spring Boot, enabling teams to manage issues and collaborate through comments.

## Features

- Create, update, assign, and close issues
- Add comments to issues for team collaboration
- Retrieve comments in chronological order
- Input validation and error handling

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

The application starts on `http://localhost:8080`.

## API Endpoints

### Issues

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/issues | Retrieve all issues |
| GET | /api/issues/{id} | Retrieve a specific issue |
| POST | /api/issues | Create a new issue |
| PUT | /api/issues/{id} | Update an existing issue |
| DELETE | /api/issues/{id} | Delete an issue |

### Comments

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/issues/{issueId}/comments | Add a comment to an issue |
| GET | /api/issues/{issueId}/comments | Get all comments for an issue (chronological order) |
| GET | /api/comments/{commentId} | Get a specific comment by ID |

## Comment API Details

### Create a Comment

**POST** `/api/issues/{issueId}/comments`

**Request Body:**
```json
{
  "text": "This issue is reproducible on version 2.1.0",
  "author": "jane.doe"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "text": "This issue is reproducible on version 2.1.0",
  "author": "jane.doe",
  "createdAt": "2024-01-15T10:30:00",
  "issueId": 42
}
```

**Validation Rules:**
- `text` must not be blank or empty
- `author` must not be blank or empty

**Error Responses:**
- `400 Bad Request` — if comment text or author is empty
- `500 Internal Server Error` — unexpected server error

---

### Get All Comments for an Issue

**GET** `/api/issues/{issueId}/comments`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "text": "Initial investigation started.",
    "author": "john.doe",
    "createdAt": "2024-01-15T09:00:00",
    "issueId": 42
  },
  {
    "id": 2,
    "text": "Root cause identified.",
    "author": "jane.doe",
    "createdAt": "2024-01-15T10:30:00",
    "issueId": 42
  }
]
```

Comments are returned in ascending chronological order by creation timestamp.

---

### Get a Specific Comment

**GET** `/api/comments/{commentId}`

**Response (200 OK):**
```json
{
  "id": 1,
  "text": "Initial investigation started.",
  "author": "john.doe",
  "createdAt": "2024-01-15T09:00:00",
  "issueId": 42
}
```

**Error Responses:**
- `404 Not Found` — if the comment does not exist

---

## Data Model

### Comment

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Auto-generated primary key |
| text | String | The comment content (required, non-empty) |
| author | String | The author of the comment (required, non-empty) |
| createdAt | LocalDateTime | Timestamp set automatically on creation |
| issueId | Long | Foreign key reference to the associated issue |

## Backward Compatibility

All existing Issue API endpoints remain fully backward compatible. No changes have been made to existing Issue request or response structures.

## Database

The application uses an in-memory H2 database by default. The schema is automatically managed by Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

### Comment Table

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

## H2 Console

Available at `http://localhost:8080/h2-console` (development only).

- JDBC URL: `jdbc:h2:mem:issuetracker`
- Username: `sa`
- Password: *(empty)*