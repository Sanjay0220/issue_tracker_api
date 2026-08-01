# Implementation in java

User Stories:
As a project team member,

I want to add comments to an issue,

So that team members can collaborate, discuss progress, and provide updates without using external communication channels.

Currently, the Issue Tracker application allows users to create, update, assign, and close issues. However, there is no mechanism for users to communicate directly within an issue.

The new feature should introduce comments that are associated with an issue and visible to all authenticated users with access to that issue.

Each comment should capture the author, timestamp, and comment text.

Comments should be displayed in chronological order.

The existing Issue APIs should remain backward compatible.

The repository should also be updated with documentation describing the new API endpoints.



Acceptance Criteria

Users can add one or more comments to an existing issue.

Every comment must contain:

Author

Timestamp

Comment Text

Comments must be associated with exactly one Issue.

Comments must be displayed in chronological order.

Existing Issue APIs must continue to function without breaking changes.

Validation should prevent empty comments.

Documentation must be updated to include the new feature.

Unit tests are not required because testing will be handled by CI.

// TODO: Implement the code generation logic here.