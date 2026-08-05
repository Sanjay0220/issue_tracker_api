package com.issuetracker.service;

import com.issuetracker.dto.CommentDTO;
import com.issuetracker.dto.CreateCommentRequest;
import com.issuetracker.entity.Comment;
import com.issuetracker.exception.EmptyCommentException;
import com.issuetracker.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Service layer for managing Comment operations.
 * Handles business logic for creating and retrieving comments associated with issues.
 */
@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;

    /**
     * Constructs a CommentService with the required repository dependency.
     *
     * @param commentRepository the repository for comment persistence
     */
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Creates and persists a new comment for the specified issue.
     *
     * @param issueId the ID of the issue to associate the comment with
     * @param request the request DTO containing comment details
     * @return a CommentDTO representing the persisted comment
     * @throws EmptyCommentException if the comment text is null or blank
     */
    @Transactional
    public CommentDTO createComment(Long issueId, CreateCommentRequest request) {
        logger.info("Creating comment for issueId={}, author={}", issueId, request.getAuthor());

        if (request.getCommentText() == null || request.getCommentText().trim().isEmpty()) {
            logger.warn("Attempt to create empty comment for issueId={}", issueId);
            throw new EmptyCommentException("Comment text must not be empty.");
        }

        if (request.getAuthor() == null || request.getAuthor().trim().isEmpty()) {
            logger.warn("Attempt to create comment with empty author for issueId={}", issueId);
            throw new IllegalArgumentException("Author must not be empty.");
        }

        Comment comment = new Comment();
        comment.setIssueId(issueId);
        comment.setAuthor(request.getAuthor().trim());
        comment.setCommentText(request.getCommentText().trim());
        comment.setCreatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        logger.info("Comment created successfully with id={} for issueId={}", saved.getId(), issueId);

        return toDTO(saved);
    }

    /**
     * Retrieves all comments for a specific issue in chronological order.
     *
     * @param issueId the ID of the issue
     * @return a list of CommentDTOs ordered by creation timestamp ascending
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByIssueId(Long issueId) {
        logger.info("Retrieving comments for issueId={}", issueId);
        List<Comment> comments = commentRepository.findByIssueIdOrderByCreatedAtAsc(issueId);
        logger.info("Found {} comment(s) for issueId={}", comments.size(), issueId);
        return comments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific comment by its ID.
     *
     * @param commentId the ID of the comment to retrieve
     * @return a CommentDTO representing the found comment
     * @throws NoSuchElementException if no comment with the given ID exists
     */
    @Transactional(readOnly = true)
    public CommentDTO getCommentById(Long commentId) {
        logger.info("Retrieving comment with id={}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    logger.warn("Comment not found with id={}", commentId);
                    return new NoSuchElementException("Comment not found with id: " + commentId);
                });
        return toDTO(comment);
    }

    /**
     * Maps a Comment entity to a CommentDTO.
     *
     * @param comment the Comment entity to map
     * @return the corresponding CommentDTO
     */
    private CommentDTO toDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setIssueId(comment.getIssueId());
        dto.setAuthor(comment.getAuthor());
        dto.setCommentText(comment.getCommentText());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}