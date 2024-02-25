package edu.java.client.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RepositoryDto {
    private long id;
    private String name;
    private UserDto owner;

    @JsonProperty("pushed_at")
    private OffsetDateTime pushedAt;
    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;
}
