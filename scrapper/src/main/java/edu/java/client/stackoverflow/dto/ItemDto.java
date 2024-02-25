package edu.java.client.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDto {
    @JsonProperty("question_id")
    private long questionId;
    private UserDto owner;
    @JsonProperty("last_activity_date")
    private OffsetDateTime lastActivity;

}
