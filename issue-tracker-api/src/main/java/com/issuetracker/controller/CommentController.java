package com.issuetracker.controller;

import com.issuetracker.dto.CommentRequestDTO;
import com.issuetracker.dto.CommentResponseDTO;
import com.issuetracker.service.CommentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Comments associated with Issues.
 * Exposes endpoints for adding and retrieving comments on issues.
 */
@RestController
@RequestMapping("/api/issues")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    /**
     * Constructs a CommentController with the required service.
     *
     * @param commentService the service handling comment business logic
     */
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Adds a new comment to the specified issue.
     *
     * @param issueId           the ID of the issue to add the comment to
     * @param commentRequestDTO the request body containing comment details
     * @return a ResponseEntity containing the created CommentResponseDTO with HTTP 201 Created
     */
    @PostMapping("/{issueId}/comments")
    public ResponseEntity<CommentResponseDTO> addComment(
            @PathVariable Long issueId,
            @Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        logger.info("POST /api/issues/{}/comments - Adding comment", issueId);
        CommentResponseDTO response = commentService.addComment(issueId, commentRequestDTO);
        logger.info("Comment created with ID: {} for issue ID: {}", response.getId(), issueId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves all comments for the specified issue in chronological order.
     *
     * @param issueId the ID of the issue whose comments are to be retrieved
     * @return a ResponseEntity containing a list of CommentResponseDTO objects with HTTP 200 OK
     */
    @GetMapping("/{issueId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByIssueId(
            @PathVariable Long issueId) {
        logger.info("GET /api/issues/{}/comments - Retrieving comments", issueId);
        List<CommentResponseDTO> comments = commentService.getCommentsByIssueId(issueId);
        logger.info("Returning {} comment(s) for issue ID: {}", comments.size(), issueId);
        return ResponseEntity.ok(comments);
    }
}