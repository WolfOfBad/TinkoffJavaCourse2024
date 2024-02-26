package edu.java.scrapper.client.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.java.client.github.dto.RepositoryDto;
import edu.java.client.github.dto.UserDto;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class GithubDtoTest {
    private static ObjectMapper mapper;

    @BeforeAll
    public static void before() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void repositoryDtoParsingTest() throws JsonProcessingException {
        String json = """
            {
                "id":123,
                "name":"rep",
                "owner": {
                },
                "pushed_at":"2024-02-25T18:46:44Z",
                "updated_at":"2024-02-02T09:41:40Z"
            }
            """;

        RepositoryDto dto = mapper.readValue(json, RepositoryDto.class);

        assertThat(dto.id()).isEqualTo(123);
        assertThat(dto.name()).isEqualTo("rep");
        assertThat(dto.owner()).isNotNull();
        assertThat(dto.pushedAt()).isEqualTo(OffsetDateTime.parse("2024-02-25T18:46:44Z"));
        assertThat(dto.updatedAt()).isEqualTo(OffsetDateTime.parse("2024-02-02T09:41:40Z"));
    }

    @Test
    public void userDtoTest() throws JsonProcessingException {
        String json = """
            {
                "id":123,
                "login":"user"
            }
            """;

        UserDto dto = mapper.readValue(json, UserDto.class);

        assertThat(dto.id()).isEqualTo(123);
        assertThat(dto.login()).isEqualTo("user");
    }

    @Test
    public void repositoryDtoBuilderTest() {
        UserDto user = new UserDto(123, "user");
        OffsetDateTime pushed = OffsetDateTime.parse("2024-02-25T18:46:44Z");
        OffsetDateTime updated = OffsetDateTime.parse("2024-02-02T09:41:40Z");

        RepositoryDto dto = RepositoryDto.builder()
            .id(1)
            .name("rep")
            .owner(user)
            .updatedAt(updated)
            .pushedAt(pushed)
            .build();

        assertThat(dto.id()).isEqualTo(1);
        assertThat(dto.name()).isEqualTo("rep");
        assertThat(dto.owner()).isEqualTo(user);
        assertThat(dto.updatedAt()).isEqualTo(updated);
        assertThat(dto.pushedAt()).isEqualTo(pushed);
    }

    @Test
    public void userDtoBuilderTest() {
        UserDto dto = UserDto.builder()
            .id(123)
            .login("user")
            .build();

        assertThat(dto.id()).isEqualTo(123);
        assertThat(dto.login()).isEqualTo("user");
    }

}
