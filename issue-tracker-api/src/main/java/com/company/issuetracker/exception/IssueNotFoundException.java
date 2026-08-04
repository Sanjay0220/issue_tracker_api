package com.company.issuetracker.exception;

/**
 * Exception thrown when a requested Issue cannot be found in the system.
 */
public class IssueNotFoundException extends RuntimeException {

    /**
     * Constructs an IssueNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public IssueNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs an IssueNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public IssueNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}