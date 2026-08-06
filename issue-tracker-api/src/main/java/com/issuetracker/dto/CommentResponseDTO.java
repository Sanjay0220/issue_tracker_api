package com.issuetracker.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for outgoing comment responses.
 * Contains all comment details returned to the client including
 * id, author, comment text, timestamp, and the associated issue ID.
 */
public class CommentResponseDTO {

    private Long id;
    private String author;
    private String commentText;
    private LocalDateTime createdAt;
    private Long issueId;

    /**
     * Default constructor.
     */
    public CommentResponseDTO() {
    }

    /**
     * Constructs a CommentResponseDTO with all fields.
     *
     * @param id          the unique identifier of the comment
     * @param author      the author of the comment
     * @param commentText the text content of the comment
     * @param createdAt   the timestamp when the comment was created
     * @param issueId     the ID of the issue this comment belongs to
     */
    public CommentResponseDTO(Long id, String author, String commentText, LocalDateTime createdAt, Long issueId) {
        this.id = id;
        this.author = author;
        this.commentText = commentText;
        this.createdAt = createdAt;
        this.issueId = issueId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }
}