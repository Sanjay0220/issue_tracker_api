package com.issuetracker.repository;

import com.issuetracker.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Comment entity persistence operations.
 * Provides CRUD operations and custom query methods for comments.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Retrieves all comments associated with a specific issue,
     * ordered by creation timestamp in ascending (chronological) order.
     *
     * @param issueId the ID of the issue
     * @return list of comments for the issue in chronological order
     */
    List<Comment> findByIssueIdOrderByCreatedAtAsc(Long issueId);

    /**
     * Checks whether any comments exist for a given issue ID.
     *
     * @param issueId the ID of the issue
     * @return true if at least one comment exists for the issue
     */
    boolean existsByIssueId(Long issueId);
}