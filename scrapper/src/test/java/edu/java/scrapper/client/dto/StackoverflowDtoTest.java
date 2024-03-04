package edu.java.scrapper.client.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.java.scrapper.client.stackoverflow.dto.ItemDto;
import edu.java.scrapper.client.stackoverflow.dto.QuestionDto;
import edu.java.scrapper.client.stackoverflow.dto.UserDto;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class StackoverflowDtoTest {
    private static ObjectMapper mapper;

    @BeforeAll
    public static void before() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void itemDtoTest() throws JsonProcessingException {
        String json = """
            {
                "question_id":123,
                "owner": {
                },
                "last_activity_date":1000
            }
            """;

        ItemDto dto = mapper.readValue(json, ItemDto.class);

        assertThat(dto.questionId()).isEqualTo(123);
        assertThat(dto.owner()).isNotNull();
        assertThat(dto.lastActivity()).isEqualTo(OffsetDateTime.ofInstant(
            Instant.ofEpochSecond(1000),
            ZoneOffset.UTC
        ));
    }

    @Test
    public void userDtoTest() throws JsonProcessingException {
        String json = """
            {
                "account_id":123,
                "display_name":"user"
            }
            """;

        UserDto dto = mapper.readValue(json, UserDto.class);

        assertThat(dto.id()).isEqualTo(123);
        assertThat(dto.name()).isEqualTo("user");
    }

    @Test
    public void questionDtoTest() throws JsonProcessingException {
        String json = """
            {
                "items":[
                ]
            }
            """;

        QuestionDto dto = mapper.readValue(json, QuestionDto.class);

        assertThat(dto.items()).isNotNull();
    }

    @Test
    public void itemDtoBuilderTest() {
        UserDto user = new UserDto(1, "user");
        OffsetDateTime updated = OffsetDateTime.parse("2024-02-25T18:46:44Z");

        ItemDto dto = ItemDto.builder()
            .owner(user)
            .lastActivity(updated)
            .questionId(123)
            .build();

        assertThat(dto.owner()).isEqualTo(user);
        assertThat(dto.lastActivity()).isEqualTo(updated);
        assertThat(dto.questionId()).isEqualTo(123);
    }

    @Test
    public void questionDtoBuilderTest() {
        List<ItemDto> items = new ArrayList<>();

        QuestionDto dto = QuestionDto.builder()
            .items(items)
            .build();

        assertThat(dto.items()).isEqualTo(items);
    }

    @Test
    public void userDtoBuilderTest() {
        UserDto dto = UserDto.builder()
            .id(123)
            .name("user")
            .build();

        assertThat(dto.name()).isEqualTo("user");
        assertThat(dto.id()).isEqualTo(123);
    }

}
