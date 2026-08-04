package com.company.issuetracker.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Transfer Object for returning Issue data in API responses.
 * Includes the labels field so clients can see all labels attached to an issue.
 */
public class IssueResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private Set<String> labels = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Default constructor.
     */
    public IssueResponseDTO() {
    }

    /**
     * Constructs an IssueResponseDTO with all fields.
     *
     * @param id          the issue ID
     * @param title       the issue title
     * @param description the issue description
     * @param status      the issue status
     * @param priority    the issue priority
     * @param labels      the set of labels
     * @param createdAt   the creation timestamp
     * @param updatedAt   the last update timestamp
     */
    public IssueResponseDTO(Long id, String title, String description, String status,
                             String priority, Set<String> labels,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.labels = labels != null ? new HashSet<>(labels) : new HashSet<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels != null ? new HashSet<>(labels) : new HashSet<>();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}