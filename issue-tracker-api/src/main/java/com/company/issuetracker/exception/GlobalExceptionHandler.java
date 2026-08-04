package com.company.issuetracker.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Issue Tracker API.
 * Provides consistent error responses across all controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles IssueNotFoundException and returns a 404 Not Found response.
     *
     * @param ex the exception
     * @return error response with 404 status
     */
    @ExceptionHandler(IssueNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleIssueNotFoundException(IssueNotFoundException ex) {
        logger.error("Issue not found: {}", ex.getMessage());
        Map<String, Object> errorBody = buildErrorBody(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }

    /**
     * Handles DuplicateLabelException and returns a 400 Bad Request response.
     *
     * @param ex the exception
     * @return error response with 400 status
     */
    @ExceptionHandler(DuplicateLabelException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateLabelException(DuplicateLabelException ex) {
        logger.error("Duplicate label error: {}", ex.getMessage());
        Map<String, Object> errorBody = buildErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    /**
     * Handles Bean Validation errors and returns a 400 Bad Request response
     * with field-level error details.
     *
     * @param ex the exception containing validation errors
     * @return error response with 400 status and field error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        logger.error("Validation error: {}", ex.getMessage());
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        Map<String, Object> errorBody = buildErrorBody(HttpStatus.BAD_REQUEST, "Validation failed");
        errorBody.put("fieldErrors", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    /**
     * Handles all unhandled exceptions and returns a 500 Internal Server Error response.
     *
     * @param ex the exception
     * @return error response with 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        Map<String, Object> errorBody = buildErrorBody(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }

    /**
     * Builds a standard error response body.
     *
     * @param status  the HTTP status
     * @param message the error message
     * @return map representing the error body
     */
    private Map<String, Object> buildErrorBody(HttpStatus status, String message) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now().toString());
        errorBody.put("status", status.value());
        errorBody.put("error", status.getReasonPhrase());
        errorBody.put("message", message);
        return errorBody;
    }
}