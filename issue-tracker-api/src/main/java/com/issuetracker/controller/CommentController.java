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
 * Exposes endpoints to create and retrieve comments while preserving
 * backward compatibility with existing Issue APIs.
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
     * Creates a new comment for the specified issue.
     *
     * @param issueId the ID of the issue to add the comment to
     * @param request the request body containing author and comment text
     * @return HTTP 201 Created with the created CommentDTO, or an error response
     */
    @PostMapping("/issues/{issueId}/comments")
    public ResponseEntity<?> createComment(
            @PathVariable Long issueId,
            @Valid @RequestBody CreateCommentRequest request) {
        logger.info("POST /api/issues/{}/comments - author={}", issueId, request.getAuthor());
        try {
            CommentDTO created = commentService.createComment(issueId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (EmptyCommentException e) {
            logger.warn("Empty comment submission for issueId={}: {}", issueId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid comment request for issueId={}: {}", issueId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error creating comment for issueId={}: {}", issueId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    /**
     * Retrieves all comments for the specified issue in chronological order.
     *
     * @param issueId the ID of the issue whose comments are to be retrieved
     * @return HTTP 200 OK with a list of CommentDTOs ordered by creation time
     */
    @GetMapping("/issues/{issueId}/comments")
    public ResponseEntity<?> getCommentsByIssueId(@PathVariable Long issueId) {
        logger.info("GET /api/issues/{}/comments", issueId);
        try {
            List<CommentDTO> comments = commentService.getCommentsByIssueId(issueId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving comments for issueId={}: {}", issueId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    /**
     * Retrieves a specific comment by its ID.
     *
     * @param commentId the ID of the comment to retrieve
     * @return HTTP 200 OK with the CommentDTO, or HTTP 404 if not found
     */
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<?> getCommentById(@PathVariable Long commentId) {
        logger.info("GET /api/comments/{}", commentId);
        try {
            CommentDTO comment = commentService.getCommentById(commentId);
            return ResponseEntity.ok(comment);
        } catch (NoSuchElementException e) {
            logger.warn("Comment not found with id={}: {}", commentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error retrieving comment with id={}: {}", commentId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}