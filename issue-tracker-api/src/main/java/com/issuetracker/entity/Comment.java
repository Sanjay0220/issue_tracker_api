package com.issuetracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Entity representing a comment associated with an Issue.
 * Each comment captures the author, timestamp, and comment text,
 * and is linked to exactly one Issue.
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
    @Column(name = "comment_text", nullable = false, columnDefinition = "TEXT")
    private String commentText;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    /**
     * Default constructor required by JPA.
     */
    public Comment() {
    }

    /**
     * Constructs a Comment with all required fields.
     *
     * @param commentText the text content of the comment
     * @param author      the author of the comment
     * @param createdAt   the timestamp when the comment was created
     * @param issue       the issue this comment is associated with
     */
    public Comment(String commentText, String author, LocalDateTime createdAt, Issue issue) {
        this.commentText = commentText;
        this.author = author;
        this.createdAt = createdAt;
        this.issue = issue;
    }

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

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }
}