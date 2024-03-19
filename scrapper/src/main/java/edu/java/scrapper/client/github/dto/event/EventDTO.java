package edu.java.scrapper.client.github.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record EventDTO(
    @NotNull
    @JsonProperty("type")
    String type,
    @JsonProperty("created_at")
    @NotNull
    OffsetDateTime createdAt,
    @JsonProperty("payload")
    PayloadDTO payload
) {
    public String getMessage() {
        return switch (getType()) {
            case PUSH -> "новый коммит в репозитории";
            case PULL_REQUEST -> pullRequestMessage();
            case PULL_REQUEST_REVIEW_COMMENT -> "новый коментарий к коду в ПР";
            case ISSUE_COMMENT -> "новый коментарий в ПР";
            case UNKNOWN -> "замечено новое обновление в репозитории";
        };
    }

    private String pullRequestMessage() {
        return switch (payload.getAction()) {
            case OPENED -> "новый ПР в репозитории";
            case CLOSED -> "один из ПР в репозитории закрыт";
            case UNKNOWN -> "замечено обновление в ПР";
        };
    }

    public EventType getType() {
        return switch (type) {
            case "PullRequestEvent" -> EventType.PULL_REQUEST;
            case "IssueCommentEvent" -> EventType.ISSUE_COMMENT;
            case "PushEvent" -> EventType.PUSH;
            case "PullRequestReviewCommentEvent" -> EventType.PULL_REQUEST_REVIEW_COMMENT;
            default -> EventType.UNKNOWN;
        };
    }

    @SuppressWarnings("MultipleStringLiterals")
    public static EventDTO getDefault() {
        return EventDTO.builder()
            .createdAt(OffsetDateTime.now())
            .type("unknown")
            .payload(PayloadDTO.builder()
                .action("unknown")
                .build())
            .build();
    }
}
