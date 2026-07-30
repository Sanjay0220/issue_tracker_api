package com.company.issuetracker.service;

import com.company.issuetracker.dto.IssueRequest;
import com.company.issuetracker.entity.Issue;
import com.company.issuetracker.entity.Project;
import com.company.issuetracker.entity.User;
import com.company.issuetracker.exception.ResourceNotFoundException;
import com.company.issuetracker.repository.IssueRepository;
import com.company.issuetracker.repository.ProjectRepository;
import com.company.issuetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public Issue createIssue(IssueRequest request, String reporterUsername) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.getProjectId()));

        User reporter = userRepository.findByUsername(reporterUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + reporterUsername));

        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found with id: " + request.getAssigneeId()));
        }

        Issue issue = Issue.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : Issue.Status.OPEN)
                .priority(request.getPriority() != null ? request.getPriority() : Issue.Priority.MEDIUM)
                .project(project)
                .reporter(reporter)
                .assignee(assignee)
                .build();

        return issueRepository.save(issue);
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    public Issue getIssueById(Long id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found with id: " + id));
    }

    public List<Issue> getIssuesByProject(Long projectId) {
        return issueRepository.findByProjectId(projectId);
    }

    public List<Issue> getIssuesByAssignee(Long assigneeId) {
        return issueRepository.findByAssigneeId(assigneeId);
    }

    public Issue updateIssue(Long id, IssueRequest request) {
        Issue issue = getIssueById(id);

        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());

        if (request.getStatus() != null) {
            issue.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            issue.setPriority(request.getPriority());
        }
        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.getProjectId()));
            issue.setProject(project);
        }
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found with id: " + request.getAssigneeId()));
            issue.setAssignee(assignee);
        }

        return issueRepository.save(issue);
    }

    public void deleteIssue(Long id) {
        Issue issue = getIssueById(id);
        issueRepository.delete(issue);
    }
}
