package com.issuetracker.repository;

import com.issuetracker.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Comment entity providing CRUD operations
 * and custom query methods for retrieving comments by issue.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Retrieves all comments associated with a given issue ID,
     * ordered by creation timestamp in ascending (chronological) order.
     *
     * @param issueId the ID of the issue
     * @return list of comments in chronological order
     */
    List<Comment> findByIssueIdOrderByCreatedAtAsc(Long issueId);
}