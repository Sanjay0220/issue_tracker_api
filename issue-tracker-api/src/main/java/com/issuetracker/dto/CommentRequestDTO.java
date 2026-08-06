package com.issuetracker.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for incoming comment creation requests.
 * Captures the author and comment text provided by the client.
 */
public class CommentRequestDTO {

    @NotBlank(message = "Author must not be blank")
    private String author;

    @NotBlank(message = "Comment text must not be empty")
    private String commentText;

    /**
     * Default constructor.
     */
    public CommentRequestDTO() {
    }

    /**
     * Constructs a CommentRequestDTO with the specified author and comment text.
     *
     * @param author      the author of the comment
     * @param commentText the text content of the comment
     */
    public CommentRequestDTO(String author, String commentText) {
        this.author = author;
        this.commentText = commentText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}