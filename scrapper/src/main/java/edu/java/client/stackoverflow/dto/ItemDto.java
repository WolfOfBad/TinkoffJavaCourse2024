package edu.java.client.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemDto {
    @JsonProperty("question_id")
    private long questionId;
    private UserDto owner;
    @JsonProperty("last_activity_date")
    private OffsetDateTime lastActivity;

}
