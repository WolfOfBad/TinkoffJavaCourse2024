package edu.java.client.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
