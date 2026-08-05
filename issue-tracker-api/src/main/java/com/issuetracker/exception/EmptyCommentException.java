package com.issuetracker.exception;

/**
 * Exception thrown when an attempt is made to create a comment with empty or blank text.
 * This is a runtime exception indicating a validation failure for comment content.
 */
public class EmptyCommentException extends RuntimeException {

    /**
     * Constructs an EmptyCommentException with the specified detail message.
     *
     * @param message the detail message describing the reason for the exception
     */
    public EmptyCommentException(String message) {
        super(message);
    }

    /**
     * Constructs an EmptyCommentException with the specified detail message and cause.
     *
     * @param message the detail message describing the reason for the exception
     * @param cause   the underlying cause of the exception
     */
    public EmptyCommentException(String message, Throwable cause) {
        super(message, cause);
    }
}