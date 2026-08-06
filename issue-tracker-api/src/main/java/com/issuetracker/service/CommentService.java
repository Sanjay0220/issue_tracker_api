package com.issuetracker.service;

import com.issuetracker.dto.CommentRequestDTO;
import com.issuetracker.dto.CommentResponseDTO;
import com.issuetracker.entity.Comment;
import com.issuetracker.entity.Issue;
import com.issuetracker.exception.EmptyCommentException;
import com.issuetracker.repository.CommentRepository;
import com.issuetracker.repository.IssueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for business logic related to Comments.
 * Handles creation and retrieval of comments associated with issues.
 */
@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;

    /**
     * Constructs a CommentService with the required repositories.
     *
     * @param commentRepository the repository for Comment persistence
     * @param issueRepository   the repository for Issue persistence
     */
    @Autowired
    public CommentService(CommentRepository commentRepository, IssueRepository issueRepository) {
        this.commentRepository = commentRepository;
        this.issueRepository = issueRepository;
    }

    /**
     * Adds a new comment to an existing issue.
     *
     * @param issueId           the ID of the issue to add the comment to
     * @param commentRequestDTO the request DTO containing comment details
     * @return a CommentResponseDTO representing the persisted comment
     * @throws jakarta.persistence.EntityNotFoundException if the issue does not exist
     * @throws EmptyCommentException                       if the comment text is blank or empty
     */
    @Transactional
    public CommentResponseDTO addComment(Long issueId, CommentRequestDTO commentRequestDTO) {
        logger.info("Adding comment to issue with ID: {}", issueId);

        if (commentRequestDTO.getCommentText() == null || commentRequestDTO.getCommentText().trim().isEmpty()) {
            logger.warn("Attempt to add empty comment to issue ID: {}", issueId);
            throw new EmptyCommentException("Comment text must not be empty");
        }

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> {
                    logger.error("Issue not found with ID: {}", issueId);
                    return new jakarta.persistence.EntityNotFoundException("Issue not found with ID: " + issueId);
                });

        Comment comment = new Comment(
                commentRequestDTO.getCommentText().trim(),
                commentRequestDTO.getAuthor(),
                LocalDateTime.now(),
                issue
        );

        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment with ID: {} successfully added to issue ID: {}", savedComment.getId(), issueId);

        return mapToResponseDTO(savedComment);
    }

    /**
     * Retrieves all comments for a given issue in chronological order.
     *
     * @param issueId the ID of the issue whose comments are to be retrieved
     * @return a list of CommentResponseDTO objects ordered by creation timestamp ascending
     * @throws jakarta.persistence.EntityNotFoundException if the issue does not exist
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getCommentsByIssueId(Long issueId) {
        logger.info("Retrieving comments for issue with ID: {}", issueId);

        if (!issueRepository.existsById(issueId)) {
            logger.error("Issue not found with ID: {}", issueId);
            throw new jakarta.persistence.EntityNotFoundException("Issue not found with ID: " + issueId);
        }

        List<Comment> comments = commentRepository.findByIssueIdOrderByCreatedAtAsc(issueId);
        logger.info("Found {} comment(s) for issue ID: {}", comments.size(), issueId);

        return comments.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Maps a Comment entity to a CommentResponseDTO.
     *
     * @param comment the Comment entity to map
     * @return the corresponding CommentResponseDTO
     */
    private CommentResponseDTO mapToResponseDTO(Comment comment) {
        CommentResponseDTO responseDTO = new CommentResponseDTO();
        responseDTO.setId(comment.getId());
        responseDTO.setCommentText(comment.getCommentText());
        responseDTO.setAuthor(comment.getAuthor());
        responseDTO.setCreatedAt(comment.getCreatedAt());
        responseDTO.setIssueId(comment.getIssue().getId());
        return responseDTO;
    }
}