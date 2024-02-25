package edu.java.scrapper.client.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.java.client.stackoverflow.dto.ItemDto;
import edu.java.client.stackoverflow.dto.QuestionDto;
import edu.java.client.stackoverflow.dto.UserDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

        assertThat(dto.getQuestionId()).isEqualTo(123);
        assertThat(dto.getOwner()).isNotNull();
        assertThat(dto.getLastActivity()).isEqualTo(OffsetDateTime.ofInstant(
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

        assertThat(dto.getId()).isEqualTo(123);
        assertThat(dto.getName()).isEqualTo("user");
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

        assertThat(dto.getItems()).isNotNull();
    }

}
