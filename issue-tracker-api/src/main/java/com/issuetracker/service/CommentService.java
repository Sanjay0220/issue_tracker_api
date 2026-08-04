package com.issuetracker.service;

import com.issuetracker.dto.CommentRequestDTO;
import com.issuetracker.dto.CommentResponseDTO;
import com.issuetracker.entity.Comment;
import com.issuetracker.exception.EmptyCommentException;
import com.issuetracker.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Service class responsible for comment business logic.
 * Handles creation and retrieval of comments associated with issues.
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
     * Creates a new comment on the specified issue.
     * Validates that the comment text is not empty before persisting.
     *
     * @param issueId           the ID of the issue to add the comment to
     * @param commentRequestDTO the request DTO containing comment text and author
     * @return the created comment as a response DTO
     * @throws EmptyCommentException if the comment text is blank or empty
     */
    @Transactional
    public CommentResponseDTO createComment(Long issueId, CommentRequestDTO commentRequestDTO) {
        logger.info("Creating comment for issueId={}, author={}", issueId, commentRequestDTO.getAuthor());

        if (commentRequestDTO.getText() == null || commentRequestDTO.getText().trim().isEmpty()) {
            logger.warn("Attempted to create an empty comment for issueId={}", issueId);
            throw new EmptyCommentException("Comment text must not be empty");
        }

        if (commentRequestDTO.getAuthor() == null || commentRequestDTO.getAuthor().trim().isEmpty()) {
            logger.warn("Attempted to create a comment with empty author for issueId={}", issueId);
            throw new IllegalArgumentException("Author must not be empty");
        }

        Comment comment = new Comment(
                commentRequestDTO.getText().trim(),
                commentRequestDTO.getAuthor().trim(),
                issueId
        );

        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment created successfully with id={} for issueId={}", savedComment.getId(), issueId);

        return mapToResponseDTO(savedComment);
    }

    /**
     * Retrieves all comments for the specified issue in chronological order.
     *
     * @param issueId the ID of the issue
     * @return list of comment response DTOs ordered by creation timestamp ascending
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getCommentsByIssueId(Long issueId) {
        logger.info("Retrieving comments for issueId={}", issueId);

        List<Comment> comments = commentRepository.findByIssueIdOrderByCreatedAtAsc(issueId);
        logger.info("Found {} comment(s) for issueId={}", comments.size(), issueId);

        return comments.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific comment by its ID.
     *
     * @param commentId the ID of the comment to retrieve
     * @return the comment response DTO
     * @throws NoSuchElementException if no comment exists with the given ID
     */
    @Transactional(readOnly = true)
    public CommentResponseDTO getCommentById(Long commentId) {
        logger.info("Retrieving comment with id={}", commentId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    logger.warn("Comment not found with id={}", commentId);
                    return new NoSuchElementException("Comment not found with id: " + commentId);
                });

        logger.info("Comment retrieved successfully with id={}", commentId);
        return mapToResponseDTO(comment);
    }

    /**
     * Maps a Comment entity to a CommentResponseDTO.
     *
     * @param comment the comment entity
     * @return the corresponding response DTO
     */
    private CommentResponseDTO mapToResponseDTO(Comment comment) {
        return new CommentResponseDTO(
                comment.getId(),
                comment.getText(),
                comment.getAuthor(),
                comment.getCreatedAt(),
                comment.getIssueId()
        );
    }
}