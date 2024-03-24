package edu.java.scrapper.client.github.dto.event;

public enum EventType {
    PULL_REQUEST,
    PULL_REQUEST_REVIEW_COMMENT,
    PUSH,
    ISSUE_COMMENT,
    UNKNOWN;

    public static EventType getEventType(String type) {
        return switch (type) {
            case "PullRequestEvent" -> EventType.PULL_REQUEST;
            case "IssueCommentEvent" -> EventType.ISSUE_COMMENT;
            case "PushEvent" -> EventType.PUSH;
            case "PullRequestReviewCommentEvent" -> EventType.PULL_REQUEST_REVIEW_COMMENT;
            default -> EventType.UNKNOWN;
        };
    }
}
