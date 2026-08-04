package com.issuetracker.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object representing the request body for creating a new comment.
 */
public class CreateCommentRequest {

    @NotBlank(message = "Comment text must not be empty")
    private String text;

    @NotBlank(message = "Author must not be empty")
    private String author;

    /**
     * Default constructor.
     */
    public CreateCommentRequest() {
    }

    /**
     * Constructs a CreateCommentRequest with all fields.
     *
     * @param text   the comment text
     * @param author the author of the comment
     */
    public CreateCommentRequest(String text, String author) {
        this.text = text;
        this.author = author;
    }

    /**
     * Returns the comment text.
     *
     * @return the comment text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the comment text.
     *
     * @param text the comment text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the author of the comment.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the comment.
     *
     * @param author the author
     */
    public void setAuthor(String author) {
        this.author = author;
    }
}