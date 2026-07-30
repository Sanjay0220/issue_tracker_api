package com.company.issuetracker.repository;

import com.company.issuetracker.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByProjectId(Long projectId);
    List<Issue> findByAssigneeId(Long assigneeId);
    List<Issue> findByReporterId(Long reporterId);
    List<Issue> findByStatus(Issue.Status status);
}
