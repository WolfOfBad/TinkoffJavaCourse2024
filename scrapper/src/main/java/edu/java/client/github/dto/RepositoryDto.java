package edu.java.client.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record RepositoryDto(
    long id,
    String name,
    UserDto owner,

    @JsonProperty("pushed_at")
    OffsetDateTime pushedAt,
    @JsonProperty("updated_at")
    OffsetDateTime updatedAt
) {

}
