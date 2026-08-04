# Issue Tracker API

A Spring Boot REST API for tracking issues with support for labels and label-based filtering.

---

## Features

- Create, retrieve, update, and delete issues
- Assign multiple labels to issues
- Filter issues by one or more labels
- Duplicate label validation
- Consistent error handling and validation

---

## Technology Stack

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 In-Memory Database
- Flyway Database Migration
- Maven

---

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8 or higher

### Build and Run

```bash
cd issue-tracker-api
mvn clean install
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## API Endpoints

### Create Issue

```
POST /api/issues
```

**Request Body:**

```json
{
  "title": "Fix login bug",
  "description": "Users cannot log in with special characters in password",
  "status": "OPEN",
  "priority": "HIGH",
  "labels": ["bug", "backend", "urgent"]
}
```

**Response:** `201 Created`

```json
{
  "id": 1,
  "title": "Fix login bug",
  "description": "Users cannot log in with special characters in password",
  "status": "OPEN",
  "priority": "HIGH",
  "labels": ["bug", "backend", "urgent"],
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

---

### Get Issue by ID

```
GET /api/issues/{id}
```

**Response:** `200 OK`

```json
{
  "id": 1,
  "title": "Fix login bug",
  "description": "Users cannot log in with special characters in password",
  "status": "OPEN",
  "priority": "HIGH",
  "labels": ["bug", "backend", "urgent"],
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

---

### Get All Issues (with optional label filtering)

```
GET /api/issues
GET /api/issues?label=backend
GET /api/issues?label=backend&label=urgent
```

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "title": "Fix login bug",
    "status": "OPEN",
    "priority": "HIGH",
    "labels": ["bug", "backend", "urgent"],
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
]
```

---

### Update Issue

```
PUT /api/issues/{id}
```

**Request Body:**

```json
{
  "title": "Fix login bug",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "labels": ["bug", "backend"]
}
```

**Response:** `200 OK`

---

### Delete Issue

```
DELETE /api/issues/{id}
```

**Response:** `204 No Content`

---

## Label Filtering

Multiple labels use AND semantics — only issues containing **all** specified labels are returned.

```
GET /api/issues?label=backend&label=urgent
```

Returns only issues that have **both** `backend` and `urgent` labels.

---

## Error Responses

| Status | Description                          |
|--------|--------------------------------------|
| 400    | Validation error or duplicate labels |
| 404    | Issue not found                      |
| 500    | Internal server error                |

**Example error response:**

```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Duplicate labels are not allowed. Please provide unique labels only."
}
```

---

## H2 Console

Available at `http://localhost:8080/h2-console` during development.

- JDBC URL: `jdbc:h2:mem:issuetracker`
- Username: `sa`
- Password: *(empty)*

---

## Project Structure

```
issue-tracker-api/
├── src/
│   └── main/
│       ├── java/com/company/issuetracker/
│       │   ├── IssueTrackerApiApplication.java
│       │   ├── controller/
│       │   │   └── IssueController.java
│       │   ├── service/
│       │   │   └── IssueService.java
│       │   ├── repository/
│       │   │   └── IssueRepository.java
│       │   ├── entity/
│       │   │   └── Issue.java
│       │   ├── dto/
│       │   │   ├── IssueRequestDTO.java
│       │   │   └── IssueResponseDTO.java
│       │   └── exception/
│       │       ├── IssueNotFoundException.java
│       │       ├── DuplicateLabelException.java
│       │       └── GlobalExceptionHandler.java
│       └── resources/
│           ├── application.properties
│           └── db/migration/
│               └── V1__create_issues_table.sql
└── pom.xml
```