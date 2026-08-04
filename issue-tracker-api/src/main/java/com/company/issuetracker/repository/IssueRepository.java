package com.company.issuetracker.repository;

import com.company.issuetracker.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link Issue} entities.
 * Provides standard CRUD operations and custom label-based filtering queries.
 */
@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    /**
     * Finds all issues that contain a specific label.
     *
     * @param label the label to filter by
     * @return list of issues containing the given label
     */
    @Query("SELECT DISTINCT i FROM Issue i JOIN i.labels l WHERE l = :label")
    List<Issue> findByLabel(@Param("label") String label);

    /**
     * Finds all issues that contain ALL of the specified labels (AND semantics).
     *
     * @param labels   the collection of labels that issues must contain
     * @param labelCount the number of labels to match
     * @return list of issues containing all given labels
     */
    @Query("SELECT i FROM Issue i JOIN i.labels l WHERE l IN :labels GROUP BY i HAVING COUNT(DISTINCT l) = :labelCount")
    List<Issue> findByAllLabels(@Param("labels") List<String> labels, @Param("labelCount") long labelCount);
}