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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Service class responsible for business logic related to issue comments.
 * Handles creation and retrieval of comments associated with issues.
 */
@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;

    /**
     * Constructs a CommentService with the required repository dependency.
     *
     * @param commentRepository the repository for comment persistence operations
     */
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Adds a new comment to the specified issue.
     *
     * @param issueId the ID of the issue to which the comment is being added
     * @param request the request DTO containing comment text and author
     * @return a CommentDTO representing the persisted comment
     * @throws EmptyCommentException if the comment text is null or blank
     */
    @Transactional
    public CommentDTO addComment(Long issueId, CreateCommentRequest request) {
        logger.info("Adding comment to issue with ID: {}", issueId);

        if (request.getText() == null || request.getText().trim().isEmpty()) {
            logger.warn("Attempt to add empty comment to issue with ID: {}", issueId);
            throw new EmptyCommentException("Comment text must not be empty");
        }

        if (request.getAuthor() == null || request.getAuthor().trim().isEmpty()) {
            logger.warn("Attempt to add comment with empty author to issue with ID: {}", issueId);
            throw new IllegalArgumentException("Author must not be empty");
        }

        Comment comment = new Comment(request.getText().trim(), request.getAuthor().trim(), issueId);
        Comment savedComment = commentRepository.save(comment);

        logger.info("Comment with ID: {} successfully added to issue with ID: {}", savedComment.getId(), issueId);
        return mapToDTO(savedComment);
    }

    /**
     * Retrieves all comments for the specified issue in chronological order.
     *
     * @param issueId the ID of the issue whose comments are to be retrieved
     * @return a list of CommentDTOs ordered by creation timestamp ascending
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByIssueId(Long issueId) {
        logger.info("Retrieving comments for issue with ID: {}", issueId);

        List<Comment> comments = commentRepository.findByIssueIdOrderByCreatedAtAsc(issueId);

        logger.info("Found {} comment(s) for issue with ID: {}", comments.size(), issueId);
        return comments.stream()
                .map(this::mapToDTO)
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
        logger.info("Retrieving comment with ID: {}", commentId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    logger.warn("Comment with ID: {} not found", commentId);
                    return new NoSuchElementException("Comment not found with ID: " + commentId);
                });

        logger.info("Comment with ID: {} successfully retrieved", commentId);
        return mapToDTO(comment);
    }

    /**
     * Maps a Comment entity to a CommentDTO.
     *
     * @param comment the Comment entity to map
     * @return the corresponding CommentDTO
     */
    private CommentDTO mapToDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getText(),
                comment.getAuthor(),
                comment.getCreatedAt(),
                comment.getIssueId()
        );
    }
}