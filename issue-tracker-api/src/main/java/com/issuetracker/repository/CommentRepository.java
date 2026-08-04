package com.issuetracker.repository;

import com.issuetracker.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Comment entity persistence operations.
 * Provides methods for retrieving comments associated with a specific issue,
 * ordered chronologically.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Retrieves all comments associated with a given issue, ordered by creation timestamp ascending.
     *
     * @param issueId the ID of the issue
     * @return list of comments in chronological order
     */
    List<Comment> findByIssueIdOrderByCreatedAtAsc(Long issueId);

    /**
     * Checks whether an issue with the given ID has any associated comments.
     *
     * @param issueId the ID of the issue
     * @return true if at least one comment exists for the issue, false otherwise
     */
    boolean existsByIssueId(Long issueId);
}