package com.issuetracker.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a comment in API responses.
 */
public class CommentDTO {

    private Long id;
    private String text;
    private String author;
    private LocalDateTime createdAt;
    private Long issueId;

    /**
     * Default constructor.
     */
    public CommentDTO() {
    }

    /**
     * Constructs a CommentDTO with all fields.
     *
     * @param id        the comment ID
     * @param text      the comment text
     * @param author    the author of the comment
     * @param createdAt the creation timestamp
     * @param issueId   the associated issue ID
     */
    public CommentDTO(Long id, String text, String author, LocalDateTime createdAt, Long issueId) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.createdAt = createdAt;
        this.issueId = issueId;
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
     * Returns the creation timestamp.
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
     * Returns the associated issue ID.
     *
     * @return the issue ID
     */
    public Long getIssueId() {
        return issueId;
    }

    /**
     * Sets the associated issue ID.
     *
     * @param issueId the issue ID
     */
    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }
}