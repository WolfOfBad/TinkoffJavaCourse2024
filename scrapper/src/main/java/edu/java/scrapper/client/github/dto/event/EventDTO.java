package edu.java.scrapper.client.github.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.scrapper.client.Event;
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
) implements Event {
    @Override
    public String getMessage() {
        return switch (EventType.getEventType(type)) {
            case PUSH -> "новый коммит в репозитории";
            case PULL_REQUEST -> pullRequestMessage();
            case PULL_REQUEST_REVIEW_COMMENT -> "новый коментарий к коду в ПР";
            case ISSUE_COMMENT -> "новый коментарий в ПР";
            case UNKNOWN -> "замечено новое обновление в репозитории";
        };
    }

    public EventType getType() {
        return EventType.getEventType(type);
    }

    private String pullRequestMessage() {
        return payload.getAction().getActionMessage();
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
