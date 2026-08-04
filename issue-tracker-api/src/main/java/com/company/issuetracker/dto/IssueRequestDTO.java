package com.company.issuetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

/**
 * Data Transfer Object for creating and updating an Issue.
 * Supports an optional set of labels. Duplicate label validation
 * is enforced at the service layer.
 */
public class IssueRequestDTO {

    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    @NotBlank(message = "Status must not be blank")
    @Size(max = 50, message = "Status must not exceed 50 characters")
    private String status;

    @Size(max = 50, message = "Priority must not exceed 50 characters")
    private String priority;

    /**
     * Optional set of labels to assign to the issue.
     * Duplicate labels are rejected at the service layer.
     */
    private Set<String> labels = new HashSet<>();

    /**
     * Default constructor.
     */
    public IssueRequestDTO() {
    }

    /**
     * Constructs an IssueRequestDTO with all fields.
     *
     * @param title       the issue title
     * @param description the issue description
     * @param status      the issue status
     * @param priority    the issue priority
     * @param labels      the set of labels
     */
    public IssueRequestDTO(String title, String description, String status, String priority, Set<String> labels) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.labels = labels != null ? new HashSet<>(labels) : new HashSet<>();
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
}