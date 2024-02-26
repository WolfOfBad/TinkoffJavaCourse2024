package edu.java.client.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record ItemDto(
    @JsonProperty("question_id")
    long questionId,
    UserDto owner,
    @JsonProperty("last_activity_date")
    OffsetDateTime lastActivity
) {
}
