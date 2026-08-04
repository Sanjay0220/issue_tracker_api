package com.issuetracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Entity representing a comment associated with an issue.
 * Each comment captures the author, timestamp, and comment text,
 * and is associated with exactly one issue.
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

    @NotBlank(message = "Comment text must not be empty")
    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @NotBlank(message = "Author must not be empty")
    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "Issue ID must not be null")
    @Column(name = "issue_id", nullable = false)
    private Long issueId;

    /**
     * Default constructor required by JPA.
     */
    public Comment() {
    }

    /**
     * Constructs a Comment with all required fields.
     *
     * @param text    the comment text
     * @param author  the author of the comment
     * @param issueId the ID of the issue this comment belongs to
     */
    public Comment(String text, String author, Long issueId) {
        this.text = text;
        this.author = author;
        this.issueId = issueId;
    }

    /**
     * Sets the createdAt timestamp before persisting.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Returns the comment ID.
     *
     * @return the comment ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the comment ID.
     *
     * @param id the comment ID
     */
    public void setId(Long id) {
        this.id = id;
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

    /**
     * Returns the timestamp when the comment was created.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the ID of the issue this comment is associated with.
     *
     * @return the issue ID
     */
    public Long getIssueId() {
        return issueId;
    }

    /**
     * Sets the issue ID this comment is associated with.
     *
     * @param issueId the issue ID
     */
    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }
}