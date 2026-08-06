package com.issuetracker.repository;

import com.issuetracker.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Comment entity persistence operations.
 * Provides methods to query comments associated with a specific issue
 * in chronological order.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Retrieves all comments associated with the given issue ID,
     * ordered by creation timestamp in ascending (chronological) order.
     *
     * @param issueId the ID of the issue whose comments are to be retrieved
     * @return a list of comments ordered by createdAt ascending
     */
    List<Comment> findByIssueIdOrderByCreatedAtAsc(Long issueId);
}