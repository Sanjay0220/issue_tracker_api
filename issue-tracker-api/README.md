# Issue Tracker API

A production-ready Spring Boot REST API for managing issues with full label support and label-based filtering.

---

## Features

- Full CRUD operations for issues
- Assign multiple labels to issues
- Filter issues by one or more labels (AND semantics)
- Duplicate label validation with descriptive error messages
- Global exception handling with consistent error response format
- Flyway database migrations
- H2 in-memory database for development

---

## Technology Stack

| Technology         | Version |
|--------------------|---------|
| Java               | 17      |
| Spring Boot        | 3.2.0   |
| Spring Data JPA    | 3.2.0   |
| H2 Database        | Runtime |
| Flyway             | 9.x     |
| Maven              | 3.8+    |

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

API available at: `http://localhost:8080`

---

## REST API Reference

### POST /api/issues

Create a new issue with optional labels.

**Request:**

```json
{
  "title": "Fix authentication bug",
  "description": "Users cannot authenticate using SSO",
  "status": "OPEN",
  "priority": "HIGH",
  "labels": ["bug", "backend", "authentication"]
}
```

**Response:** `201 Created`

```json
{
  "id": 1,
  "title": "Fix authentication bug",
  "description": "Users cannot authenticate using SSO",
  "status": "OPEN",
  "priority": "HIGH",
  "labels": ["bug", "backend", "authentication"],
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

---

### GET /api/issues/{id}

Retrieve a single issue by ID. Labels are included in the response.

**Response:** `200 OK`

---

### GET /api/issues

Retrieve all issues. Supports optional label filtering.

| Query Parameter | Description                          | Example                              |
|-----------------|--------------------------------------|--------------------------------------|
| `label`         | Filter by label (repeatable for AND) | `?label=backend&label=urgent`        |

**Examples:**

```
GET /api/issues
GET /api/issues?label=backend
GET /api/issues?label=backend&label=urgent
```

**Response:** `200 OK` — list of matching issues.

---

### PUT /api/issues/{id}

Update an existing issue. Supports adding, removing, or replacing labels.

**Request:**

```json
{
  "title": "Fix authentication bug",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "labels": ["bug", "backend"]
}
```

**Response:** `200 OK`

---

### DELETE /api/issues/{id}

Delete an issue by ID.

**Response:** `204 No Content`

---

## Label Rules

- Labels are optional when creating or updating an issue.
- Multiple labels can be assigned to a single issue.
- Duplicate labels in the same request are rejected with `400 Bad Request`.
- When filtering by multiple labels, AND semantics apply — only issues containing **all** specified labels are returned.

---

## Error Handling

All errors follow a consistent response format:

```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Duplicate labels are not allowed. Please provide unique labels only."
}
```

| HTTP Status | Scenario                                    |
|-------------|---------------------------------------------|
| 400         | Validation failure or duplicate labels      |
| 404         | Issue not found                             |
| 500         | Unexpected server error                     |

---

## Database

Uses H2 in-memory database with Flyway migrations.

**H2 Console:** `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:issuetracker`
- Username: `sa`
- Password: *(empty)*

### Schema

```sql
CREATE TABLE issues (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(5000),
    status      VARCHAR(50)  NOT NULL,
    priority    VARCHAR(50),
    created_at  DATETIME     NOT NULL,
    updated_at  DATETIME     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE issue_labels (
    issue_id BIGINT       NOT NULL,
    label    VARCHAR(100) NOT NULL,
    FOREIGN KEY (issue_id) REFERENCES issues(id) ON DELETE CASCADE
);
```

---

## Project Structure

```
src/main/java/com/company/issuetracker/
├── IssueTrackerApiApplication.java
├── controller/
│   └── IssueController.java
├── service/
│   └── IssueService.java
├── repository/
│   └── IssueRepository.java
├── entity/
│   └── Issue.java
├── dto/
│   ├── IssueRequestDTO.java
│   └── IssueResponseDTO.java
└── exception/
    ├── IssueNotFoundException.java
    ├── DuplicateLabelException.java
    └── GlobalExceptionHandler.java
```