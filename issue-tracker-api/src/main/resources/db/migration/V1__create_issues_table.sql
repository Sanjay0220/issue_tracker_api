-- Migration: V1__create_issues_table.sql
-- Creates the issues table and issue_labels collection table
-- with required indexes for label-based filtering performance.

CREATE TABLE IF NOT EXISTS issues (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    title       VARCHAR(255)    NOT NULL,
    description VARCHAR(5000),
    status      VARCHAR(50)     NOT NULL,
    priority    VARCHAR(50),
    created_at  DATETIME        NOT NULL,
    updated_at  DATETIME        NOT NULL,
    CONSTRAINT pk_issues PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS issue_labels (
    issue_id    BIGINT          NOT NULL,
    label       VARCHAR(100)    NOT NULL,
    CONSTRAINT fk_issue_labels_issue
        FOREIGN KEY (issue_id) REFERENCES issues (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_issue_labels_issue_id
    ON issue_labels (issue_id);

CREATE INDEX idx_issue_labels_label
    ON issue_labels (label);