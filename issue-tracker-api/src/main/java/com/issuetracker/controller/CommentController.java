package com.issuetracker.controller;

import com.issuetracker.dto.CommentDTO;
import com.issuetracker.dto.CreateCommentRequest;
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
 * REST controller for managing comments associated with issues.
 * Provides endpoints for creating and retrieving comments.
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
     * Adds a new comment to the specified issue.
     *
     * @param issueId the ID of the issue to which the comment is being added
     * @param request the request body containing comment text and author
     * @return ResponseEntity containing the created CommentDTO with HTTP 201 status,
     *         or HTTP 400 if validation fails, or HTTP 404 if the issue is not found
     */
    @PostMapping("/issues/{issueId}/comments")
    public ResponseEntity<?> addComment(
            @PathVariable Long issueId,
            @Valid @RequestBody CreateCommentRequest request) {
        logger.info("POST /api/issues/{}/comments - Adding comment", issueId);
        try {
            CommentDTO createdComment = commentService.addComment(issueId, request);
            logger.info("Comment successfully created with ID: {} for issue ID: {}", createdComment.getId(), issueId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } catch (EmptyCommentException e) {
            logger.warn("Empty comment submission for issue ID: {} - {}", issueId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid comment request for issue ID: {} - {}", issueId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error adding comment to issue ID: {}", issueId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    /**
     * Retrieves all comments for the specified issue in chronological order.
     *
     * @param issueId the ID of the issue whose comments are to be retrieved
     * @return ResponseEntity containing a list of CommentDTOs with HTTP 200 status
     */
    @GetMapping("/issues/{issueId}/comments")
    public ResponseEntity<?> getCommentsByIssueId(@PathVariable Long issueId) {
        logger.info("GET /api/issues/{}/comments - Retrieving comments", issueId);
        try {
            List<CommentDTO> comments = commentService.getCommentsByIssueId(issueId);
            logger.info("Returning {} comment(s) for issue ID: {}", comments.size(), issueId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving comments for issue ID: {}", issueId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    /**
     * Retrieves a specific comment by its ID.
     *
     * @param commentId the ID of the comment to retrieve
     * @return ResponseEntity containing the CommentDTO with HTTP 200 status,
     *         or HTTP 404 if the comment is not found
     */
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<?> getCommentById(@PathVariable Long commentId) {
        logger.info("GET /api/comments/{} - Retrieving comment", commentId);
        try {
            CommentDTO comment = commentService.getCommentById(commentId);
            logger.info("Comment with ID: {} successfully retrieved", commentId);
            return ResponseEntity.ok(comment);
        } catch (NoSuchElementException e) {
            logger.warn("Comment not found with ID: {}", commentId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error retrieving comment with ID: {}", commentId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}