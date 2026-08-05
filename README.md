# Issue Tracker API

A RESTful API for tracking issues and enabling team collaboration through comments.

## Features

- Create, update, assign, and close issues
- Add comments to issues for team collaboration
- Retrieve comments in chronological order
- Validation to prevent empty comments

## Technology Stack

- Java
- Spring Boot
- Maven
- JPA / Hibernate
- H2 Database (in-memory, development)

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

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
| GET | /api/issues | List all issues |
| GET | /api/issues/{id} | Get issue by ID |
| POST | /api/issues | Create a new issue |
| PUT | /api/issues/{id} | Update an issue |
| DELETE | /api/issues/{id} | Delete an issue |

### Comments

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/issues/{issueId}/comments | Add a comment to an issue |
| GET | /api/issues/{issueId}/comments | Get all comments for an issue |
| GET | /api/comments/{commentId} | Get a specific comment by ID |

## Comment API Details

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
  "issueId": 42,
  "author": "Jane Doe",
  "commentText": "This issue has been reproduced on staging environment.",
  "createdAt": "2026-08-05T10:30:00"
}
```

**Validation:**
- `author` must not be blank (400 Bad Request returned if blank)
- `commentText` must not be blank (400 Bad Request returned if blank)

### Get Comments for an Issue

**GET** `/api/issues/{issueId}/comments`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "issueId": 42,
    "author": "Jane Doe",
    "commentText": "This issue has been reproduced on staging environment.",
    "createdAt": "2026-08-05T10:30:00"
  },
  {
    "id": 2,
    "issueId": 42,
    "author": "John Smith",
    "commentText": "Working on a fix now.",
    "createdAt": "2026-08-05T11:00:00"
  }
]
```

Comments are always returned in chronological order (oldest first).

### Get a Specific Comment

**GET** `/api/comments/{commentId}`

**Response (200 OK):**
```json
{
  "id": 1,
  "issueId": 42,
  "author": "Jane Doe",
  "commentText": "This issue has been reproduced on staging environment.",
  "createdAt": "2026-08-05T10:30:00"
}
```

**Error Response (404 Not Found):**
```
Comment not found with id: 1
```

## Backward Compatibility

All existing Issue API endpoints remain fully functional and unchanged. The comment feature is implemented as an additive extension and does not modify any existing Issue API contracts.

## License

This project is licensed under the MIT License.