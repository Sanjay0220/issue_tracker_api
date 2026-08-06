package com.issuetracker.exception;

/**
 * Exception thrown when an attempt is made to create a comment with empty or blank text.
 * This enforces the validation requirement that comment text must not be empty.
 */
public class EmptyCommentException extends RuntimeException {

    /**
     * Constructs an EmptyCommentException with the specified detail message.
     *
     * @param message the detail message describing the validation failure
     */
    public EmptyCommentException(String message) {
        super(message);
    }

    /**
     * Constructs an EmptyCommentException with the specified detail message and cause.
     *
     * @param message the detail message describing the validation failure
     * @param cause   the cause of this exception
     */
    public EmptyCommentException(String message, Throwable cause) {
        super(message, cause);
    }
}