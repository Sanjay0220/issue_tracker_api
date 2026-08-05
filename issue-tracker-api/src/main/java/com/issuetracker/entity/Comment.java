package com.issuetracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Entity representing a comment associated with an Issue.
 * Each comment captures the author, timestamp, and comment text,
 * and is associated with exactly one Issue.
 */
@Entity
@Table(name = "comments", indexes = {
        @Index(name = "idx_comment_issue_id", columnList = "issue_id"),
        @Index(name = "idx_comment_created_at", columnList = "created_at")
})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Issue ID must not be null")
    @Column(name = "issue_id", nullable = false)
    private Long issueId;

    @NotBlank(message = "Author must not be blank")
    @Column(name = "author", nullable = false)
    private String author;

    @NotBlank(message = "Comment text must not be blank")
    @Column(name = "comment_text", nullable = false, columnDefinition = "TEXT")
    private String commentText;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Default constructor required by JPA.
     */
    public Comment() {
    }

    /**
     * Constructs a Comment with all required fields.
     *
     * @param issueId     the ID of the associated issue
     * @param author      the author of the comment
     * @param commentText the text content of the comment
     */
    public Comment(Long issueId, String author, String commentText) {
        this.issueId = issueId;
        this.author = author;
        this.commentText = commentText;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Sets the createdAt timestamp before persisting a new entity.
     */
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
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
        return "Comment{" +
                "id=" + id +
                ", issueId=" + issueId +
                ", author='" + author + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}