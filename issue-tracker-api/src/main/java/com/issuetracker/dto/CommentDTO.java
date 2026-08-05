package com.issuetracker.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a Comment in API responses.
 * Contains all fields required to represent a comment to API consumers.
 */
public class CommentDTO {

    private Long id;
    private Long issueId;
    private String author;
    private String commentText;
    private LocalDateTime createdAt;

    /**
     * Default constructor.
     */
    public CommentDTO() {
    }

    /**
     * Constructs a CommentDTO with all fields.
     *
     * @param id          the unique identifier of the comment
     * @param issueId     the ID of the associated issue
     * @param author      the author of the comment
     * @param commentText the text content of the comment
     * @param createdAt   the timestamp when the comment was created
     */
    public CommentDTO(Long id, Long issueId, String author, String commentText, LocalDateTime createdAt) {
        this.id = id;
        this.issueId = issueId;
        this.author = author;
        this.commentText = commentText;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
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

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", issueId=" + issueId +
                ", author='" + author + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}