package com.company.issuetracker.dto;

import com.company.issuetracker.entity.Issue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private Issue.Status status;

    private Issue.Priority priority;

    @NotNull(message = "Project id is required")
    private Long projectId;

    private Long assigneeId;
}
