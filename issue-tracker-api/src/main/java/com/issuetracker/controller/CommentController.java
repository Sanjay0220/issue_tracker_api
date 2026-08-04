package com.issuetracker.controller;

import com.issuetracker.dto.CommentRequestDTO;
import com.issuetracker.dto.CommentResponseDTO;
import com.issuetracker.exception.EmptyCommentException;
import com.issuetracker.service.CommentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * REST controller for managing comments on issues.
 * Provides endpoints to create and retrieve comments associated with issues.
 */
@RestController
@RequestMapping("/api")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    /**
     * Constructs a CommentController with the required service dependency.
     *
     * @param commentService the service handling comment business logic
     */
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Creates a new comment on the specified issue.
     *
     * @param issueId           the ID of the issue to add the comment to
     * @param commentRequestDTO the request body containing comment text and author
     * @return HTTP 201 Created with the created comment, or HTTP 400 if validation fails,
     *         or HTTP 404 if the issue does not exist
     */
    @PostMapping("/issues/{issueId}/comments")
    public ResponseEntity<?> createComment(
            @PathVariable Long issueId,
            @Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        logger.info("POST /api/issues/{}/comments - Creating comment", issueId);
        try {
            CommentResponseDTO response = commentService.createComment(issueId, commentRequestDTO);
            logger.info("Comment created with id={} for issueId={}", response.getId(), issueId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EmptyCommentException ex) {
            logger.warn("Empty comment creation attempted for issueId={}: {}", issueId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.warn("Invalid comment request for issueId={}: {}", issueId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Unexpected error creating comment for issueId={}: {}", issueId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    /**
     * Retrieves all comments for the specified issue in chronological order.
     *
     * @param issueId the ID of the issue
     * @return HTTP 200 OK with the list of comments in chronological order
     */
    @GetMapping("/issues/{issueId}/comments")
    public ResponseEntity<?> getCommentsByIssueId(@PathVariable Long issueId) {
        logger.info("GET /api/issues/{}/comments - Retrieving comments", issueId);
        try {
            List<CommentResponseDTO> comments = commentService.getCommentsByIssueId(issueId);
            logger.info("Returning {} comment(s) for issueId={}", comments.size(), issueId);
            return ResponseEntity.ok(comments);
        } catch (Exception ex) {
            logger.error("Unexpected error retrieving comments for issueId={}: {}", issueId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    /**
     * Retrieves a specific comment by its ID.
     *
     * @param commentId the ID of the comment to retrieve
     * @return HTTP 200 OK with the comment, or HTTP 404 if not found
     */
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<?> getCommentById(@PathVariable Long commentId) {
        logger.info("GET /api/comments/{} - Retrieving comment", commentId);
        try {
            CommentResponseDTO comment = commentService.getCommentById(commentId);
            logger.info("Comment retrieved with id={}", commentId);
            return ResponseEntity.ok(comment);
        } catch (NoSuchElementException ex) {
            logger.warn("Comment not found with id={}: {}", commentId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Unexpected error retrieving comment with id={}: {}", commentId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}