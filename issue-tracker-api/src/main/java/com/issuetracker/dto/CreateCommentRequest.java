package com.issuetracker.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for creating a new comment.
 * Carries the author and comment text submitted by the API consumer.
 */
public class CreateCommentRequest {

    @NotBlank(message = "Author must not be blank")
    private String author;

    @NotBlank(message = "Comment text must not be blank")
    private String commentText;

    /**
     * Default constructor.
     */
    public CreateCommentRequest() {
    }

    /**
     * Constructs a CreateCommentRequest with the specified author and comment text.
     *
     * @param author      the author of the comment
     * @param commentText the text content of the comment
     */
    public CreateCommentRequest(String author, String commentText) {
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

    @Override
    public String toString() {
        return "CreateCommentRequest{" +
                "author='" + author + '\'' +
                '}';
    }
}