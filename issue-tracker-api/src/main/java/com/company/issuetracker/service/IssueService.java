package com.company.issuetracker.service;

import com.company.issuetracker.dto.IssueRequestDTO;
import com.company.issuetracker.dto.IssueResponseDTO;
import com.company.issuetracker.entity.Issue;
import com.company.issuetracker.exception.DuplicateLabelException;
import com.company.issuetracker.exception.IssueNotFoundException;
import com.company.issuetracker.repository.IssueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service layer for Issue management operations.
 * Handles business logic for creating, retrieving, updating, deleting,
 * and filtering issues, including label validation.
 */
@Service
@Transactional
public class IssueService {

    private static final Logger logger = LoggerFactory.getLogger(IssueService.class);

    private final IssueRepository issueRepository;

    /**
     * Constructs the IssueService with the required repository dependency.
     *
     * @param issueRepository the Issue JPA repository
     */
    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    /**
     * Creates a new issue from the provided request DTO.
     * Validates that no duplicate labels are present before persisting.
     *
     * @param requestDTO the issue creation request
     * @return the created issue as a response DTO
     * @throws DuplicateLabelException if duplicate labels are detected in the request
     */
    public IssueResponseDTO createIssue(IssueRequestDTO requestDTO) {
        logger.info("Creating new issue with title: {}", requestDTO.getTitle());
        validateNoDuplicateLabels(requestDTO.getLabels());

        Issue issue = new Issue(
                requestDTO.getTitle(),
                requestDTO.getDescription(),
                requestDTO.getStatus(),
                requestDTO.getPriority(),
                requestDTO.getLabels()
        );

        Issue savedIssue = issueRepository.save(issue);
        logger.info("Issue created successfully with id: {}", savedIssue.getId());
        return mapToResponseDTO(savedIssue);
    }

    /**
     * Retrieves an issue by its ID.
     *
     * @param id the issue ID
     * @return the issue as a response DTO
     * @throws IssueNotFoundException if no issue exists with the given ID
     */
    @Transactional(readOnly = true)
    public IssueResponseDTO getIssueById(Long id) {
        logger.info("Retrieving issue with id: {}", id);
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Issue not found with id: {}", id);
                    return new IssueNotFoundException("Issue not found with id: " + id);
                });
        return mapToResponseDTO(issue);
    }

    /**
     * Updates an existing issue identified by its ID.
     * Validates that no duplicate labels are present before saving.
     *
     * @param id         the issue ID to update
     * @param requestDTO the update request containing new field values and labels
     * @return the updated issue as a response DTO
     * @throws IssueNotFoundException  if no issue exists with the given ID
     * @throws DuplicateLabelException if duplicate labels are detected in the request
     */
    public IssueResponseDTO updateIssue(Long id, IssueRequestDTO requestDTO) {
        logger.info("Updating issue with id: {}", id);
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Issue not found with id: {}", id);
                    return new IssueNotFoundException("Issue not found with id: " + id);
                });

        validateNoDuplicateLabels(requestDTO.getLabels());

        issue.setTitle(requestDTO.getTitle());
        issue.setDescription(requestDTO.getDescription());
        issue.setStatus(requestDTO.getStatus());
        issue.setPriority(requestDTO.getPriority());
        issue.setLabels(requestDTO.getLabels());

        Issue updatedIssue = issueRepository.save(issue);
        logger.info("Issue updated successfully with id: {}", updatedIssue.getId());
        return mapToResponseDTO(updatedIssue);
    }

    /**
     * Deletes an issue by its ID.
     *
     * @param id the issue ID to delete
     * @throws IssueNotFoundException if no issue exists with the given ID
     */
    public void deleteIssue(Long id) {
        logger.info("Deleting issue with id: {}", id);
        if (!issueRepository.existsById(id)) {
            logger.warn("Issue not found with id: {}", id);
            throw new IssueNotFoundException("Issue not found with id: " + id);
        }
        issueRepository.deleteById(id);
        logger.info("Issue deleted successfully with id: {}", id);
    }

    /**
     * Retrieves all issues, optionally filtered by one or more labels.
     * When multiple labels are provided, only issues containing ALL specified labels are returned.
     * When no labels are provided, all issues are returned.
     *
     * @param labels optional list of labels to filter by
     * @return list of matching issues as response DTOs
     */
    @Transactional(readOnly = true)
    public List<IssueResponseDTO> getIssues(List<String> labels) {
        if (labels == null || labels.isEmpty()) {
            logger.info("Retrieving all issues");
            return issueRepository.findAll()
                    .stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
        }

        if (labels.size() == 1) {
            logger.info("Retrieving issues filtered by label: {}", labels.get(0));
            return issueRepository.findByLabel(labels.get(0))
                    .stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
        }

        logger.info("Retrieving issues filtered by labels: {}", labels);
        List<String> distinctLabels = labels.stream().distinct().collect(Collectors.toList());
        return issueRepository.findByAllLabels(distinctLabels, (long) distinctLabels.size())
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Validates that the provided set of labels contains no duplicates.
     * Since a Set inherently prevents duplicates, this method validates that
     * the raw input list (if provided as a list) does not contain repeated values.
     * For Set inputs, this is a no-op guard confirming the contract.
     *
     * @param labels the set of labels to validate
     * @throws DuplicateLabelException if duplicate label values are detected
     */
    private void validateNoDuplicateLabels(Set<String> labels) {
        if (labels == null || labels.isEmpty()) {
            return;
        }
        List<String> labelList = new ArrayList<>(labels);
        Set<String> uniqueLabels = new HashSet<>(labelList);
        if (uniqueLabels.size() != labelList.size()) {
            logger.warn("Duplicate labels detected in request: {}", labels);
            throw new DuplicateLabelException("Duplicate labels are not allowed. Please provide unique labels only.");
        }
    }

    /**
     * Maps an Issue entity to an IssueResponseDTO.
     *
     * @param issue the Issue entity
     * @return the corresponding IssueResponseDTO
     */
    private IssueResponseDTO mapToResponseDTO(Issue issue) {
        return new IssueResponseDTO(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getStatus(),
                issue.getPriority(),
                issue.getLabels(),
                issue.getCreatedAt(),
                issue.getUpdatedAt()
        );
    }
}