package com.company.issuetracker.controller;

import com.company.issuetracker.dto.IssueRequestDTO;
import com.company.issuetracker.dto.IssueResponseDTO;
import com.company.issuetracker.service.IssueService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Issue management endpoints.
 * Exposes CRUD operations and label-based filtering for issues.
 */
@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private static final Logger logger = LoggerFactory.getLogger(IssueController.class);

    private final IssueService issueService;

    /**
     * Constructs the IssueController with the required service dependency.
     *
     * @param issueService the Issue service
     */
    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    /**
     * Creates a new issue.
     * Accepts an optional list of labels in the request body.
     * Duplicate labels are rejected with a 400 Bad Request response.
     *
     * @param requestDTO the issue creation request body
     * @return 201 Created with the created issue response
     */
    @PostMapping
    public ResponseEntity<IssueResponseDTO> createIssue(@Valid @RequestBody IssueRequestDTO requestDTO) {
        logger.info("POST /api/issues - Creating issue with title: {}", requestDTO.getTitle());
        IssueResponseDTO response = issueService.createIssue(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves a single issue by its ID.
     * The response includes all labels assigned to the issue.
     *
     * @param id the issue ID
     * @return 200 OK with the issue response, or 404 Not Found if the issue does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<IssueResponseDTO> getIssueById(@PathVariable Long id) {
        logger.info("GET /api/issues/{} - Retrieving issue", id);
        IssueResponseDTO response = issueService.getIssueById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all issues, optionally filtered by one or more labels.
     * Multiple label query parameters are supported (AND semantics).
     *
     * <p>Examples:
     * <ul>
     *   <li>GET /api/issues - returns all issues</li>
     *   <li>GET /api/issues?label=backend - returns issues with label "backend"</li>
     *   <li>GET /api/issues?label=backend&amp;label=urgent - returns issues with both labels</li>
     * </ul>
     *
     * @param labels optional list of label query parameters
     * @return 200 OK with the list of matching issues
     */
    @GetMapping
    public ResponseEntity<List<IssueResponseDTO>> getIssues(
            @RequestParam(name = "label", required = false) List<String> labels) {
        if (labels != null && !labels.isEmpty()) {
            logger.info("GET /api/issues?label={} - Retrieving issues filtered by labels", labels);
        } else {
            logger.info("GET /api/issues - Retrieving all issues");
        }
        List<IssueResponseDTO> response = issueService.getIssues(labels);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing issue by its ID.
     * Supports adding, removing, or modifying labels.
     * Duplicate labels are rejected with a 400 Bad Request response.
     *
     * @param id         the issue ID to update
     * @param requestDTO the update request body
     * @return 200 OK with the updated issue response, or 404 Not Found if the issue does not exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<IssueResponseDTO> updateIssue(
            @PathVariable Long id,
            @Valid @RequestBody IssueRequestDTO requestDTO) {
        logger.info("PUT /api/issues/{} - Updating issue", id);
        IssueResponseDTO response = issueService.updateIssue(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes an issue by its ID.
     *
     * @param id the issue ID to delete
     * @return 204 No Content on success, or 404 Not Found if the issue does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        logger.info("DELETE /api/issues/{} - Deleting issue", id);
        issueService.deleteIssue(id);
        return ResponseEntity.noContent().build();
    }
}