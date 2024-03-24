package edu.java.scrapper.service;

import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.client.github.dto.RepositoryDto;
import edu.java.scrapper.client.github.dto.UserDto;
import edu.java.scrapper.client.stackoverflow.StackoverflowClient;
import edu.java.scrapper.client.stackoverflow.dto.ItemDto;
import edu.java.scrapper.client.stackoverflow.dto.QuestionDto;
import edu.java.scrapper.service.linkchecker.GithubChecker;
import edu.java.scrapper.service.linkchecker.LinkCheckerManager;
import edu.java.scrapper.service.linkchecker.StackoverflowChecker;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinkCheckerTest {
    @Mock
    private GithubClient githubClient;

    @Mock
    private StackoverflowClient stackoverflowClient;

    private LinkCheckerManager linkCheckerManager;

    @BeforeEach
    public void before() {
        linkCheckerManager = LinkCheckerManager.link(
            new GithubChecker(githubClient),
            new StackoverflowChecker(stackoverflowClient)
        );
    }

    @Test
    public void githubUpdateTest() {

        OffsetDateTime updateTime = OffsetDateTime.parse("2020-01-01T00:00:00+00:00");
        OffsetDateTime pushedTime = OffsetDateTime.parse("2021-01-01T00:00:00+00:00");
        when(githubClient.getRepository(anyString(), anyString())).thenReturn(RepositoryDto.builder()
            .id(1)
            .owner(new UserDto(1, "test"))
            .updatedAt(updateTime)
            .pushedAt(pushedTime)
            .build()
        );

        OffsetDateTime result = linkCheckerManager.check("https://github.com/WolfOfBad/TinkoffJavaCourse2024");

        assertThat(result).isEqualTo(pushedTime);
    }

    @Test
    public void stackoverflowUpdateTest() {
        OffsetDateTime updateTime = OffsetDateTime.parse("2020-01-01T00:00:00+00:00");
        when(stackoverflowClient.getQuestion(123)).thenReturn(QuestionDto.builder()
            .items(List.of(ItemDto.builder()
                .lastActivity(updateTime)
                .owner(null)
                .build()))
            .build());

        OffsetDateTime result = linkCheckerManager.check("https://stackoverflow.com/questions/123/");

        assertThat(result).isEqualTo(updateTime);
    }

    @Test
    public void wrongLinkUpdateTest() {
        assertThatThrownBy(() -> linkCheckerManager.check("wrong link"))
            .hasMessage("Wrong link format");
    }

}
