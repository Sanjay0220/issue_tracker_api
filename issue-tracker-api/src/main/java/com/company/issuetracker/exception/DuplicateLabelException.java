package com.company.issuetracker.exception;

/**
 * Exception thrown when duplicate labels are detected in an issue create or update request.
 */
public class DuplicateLabelException extends RuntimeException {

    /**
     * Constructs a DuplicateLabelException with the specified detail message.
     *
     * @param message the detail message
     */
    public DuplicateLabelException(String message) {
        super(message);
    }

    /**
     * Constructs a DuplicateLabelException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public DuplicateLabelException(String message, Throwable cause) {
        super(message, cause);
    }
}