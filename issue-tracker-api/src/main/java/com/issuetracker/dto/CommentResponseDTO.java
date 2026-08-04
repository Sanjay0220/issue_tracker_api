package com.issuetracker.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for returning comment data to the client.
 * Includes the comment ID, text, author, timestamp, and associated issue ID.
 */
public class CommentResponseDTO {

    private Long id;
    private String text;
    private String author;
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
     * @param id        the comment ID
     * @param text      the comment text
     * @param author    the author of the comment
     * @param createdAt the timestamp when the comment was created
     * @param issueId   the ID of the issue this comment belongs to
     */
    public CommentResponseDTO(Long id, String text, String author, LocalDateTime createdAt, Long issueId) {
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