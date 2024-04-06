package edu.java.scrapper.client;

import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.client.github.dto.RepositoryDto;
import edu.java.scrapper.client.github.dto.event.Action;
import edu.java.scrapper.client.github.dto.event.EventType;
import edu.java.scrapper.configuration.ApplicationConfigProperties;
import edu.java.scrapper.retry.BackoffType;
import edu.java.scrapper.retry.RetryExchangeFilter;
import edu.java.scrapper.retry.impl.ConstantBackoff;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import wiremock.com.google.common.io.Resources;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(WireMockServerExtension.class)
public class GithubClientTest {
    private ExchangeFilterFunction retryFilter = new RetryExchangeFilter(
        new ConstantBackoff(Duration.ZERO),
        new ApplicationConfigProperties.ClientProperties.BackoffConfig(
            BackoffType.CONSTANT,
            2,
            Duration.ZERO,
            new ArrayList<>()
        )
    );

    @AfterEach
    public void before() {
        WireMockServerExtension.resetAll();
    }

    @Test
    public void getRepositoryTest() throws IOException {
        String repositoryBody = Resources.toString(
            Resources.getResource("client/GithubRepositoryResponseBody.json"),
            StandardCharsets.UTF_8
        );
        WireMockServerExtension.getWireMockServer().stubFor(get(urlPathMatching("/repos/user/rep"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(repositoryBody)));

        GithubClient client = new GithubClient("http://localhost:" + WireMockServerExtension.getPort(), retryFilter);
        RepositoryDto result = client.getRepository("user", "rep");

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(751770237);
        assertThat(result.name()).isEqualTo("TinkoffJavaCourse2024");
        assertThat(result.pushedAt()).isEqualTo(OffsetDateTime.parse("2024-02-22T18:52:29Z"));
        assertThat(result.updatedAt()).isEqualTo(OffsetDateTime.parse("2024-02-02T09:41:40Z"));
        assertThat(result.owner().id()).isEqualTo(138949548);
        assertThat(result.owner().login()).isEqualTo("WolfOfBad");
    }

    @Test
    public void getEventPushTest() throws IOException {
        String repositoryBody = Resources.toString(
            Resources.getResource("client/event/GithubPushEvent.json"),
            StandardCharsets.UTF_8
        );
        WireMockServerExtension.getWireMockServer().stubFor(get(urlPathMatching("/repos/user/rep/events"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(repositoryBody)));

        GithubClient client = new GithubClient("http://localhost:" + WireMockServerExtension.getPort(), retryFilter);
        var result = client.getEvent("user", "rep");

        assertThat(result.createdAt()).isEqualTo(OffsetDateTime.parse("2024-03-18T00:00:34Z"));
        assertThat(result.getMessage()).isEqualTo("новый коммит в репозитории");
        assertThat(result.getType()).isEqualTo(EventType.PUSH);
    }

    @Test
    public void getEventIssueCommentTest() throws IOException {
        String repositoryBody = Resources.toString(
            Resources.getResource("client/event/GithubIssueCommentEvent.json"),
            StandardCharsets.UTF_8
        );
        WireMockServerExtension.getWireMockServer().stubFor(get(urlPathMatching("/repos/user/rep/events"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(repositoryBody)));

        GithubClient client = new GithubClient("http://localhost:" + WireMockServerExtension.getPort(), retryFilter);
        var result = client.getEvent("user", "rep");

        assertThat(result.createdAt()).isEqualTo(OffsetDateTime.parse("2024-03-17T20:25:24Z"));
        assertThat(result.getType()).isEqualTo(EventType.ISSUE_COMMENT);
        assertThat(result.getMessage()).isEqualTo("новый коментарий в ПР");
    }

    @Test
    public void getEventPullRequestOpenedTest() throws IOException {
        String repositoryBody = Resources.toString(
            Resources.getResource("client/event/GithubPullRequestOpenedEvent.json"),
            StandardCharsets.UTF_8
        );
        WireMockServerExtension.getWireMockServer().stubFor(get(urlPathMatching("/repos/user/rep/events"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(repositoryBody)));

        GithubClient client = new GithubClient("http://localhost:" + WireMockServerExtension.getPort(), retryFilter);
        var result = client.getEvent("user", "rep");

        assertThat(result.createdAt()).isEqualTo(OffsetDateTime.parse("2024-03-17T20:20:37Z"));
        assertThat(result.getType()).isEqualTo(EventType.PULL_REQUEST);
        assertThat(result.getMessage()).isEqualTo("новый ПР в репозитории");
        assertThat(result.payload().getAction()).isEqualTo(Action.OPENED);
    }

    @Test
    public void getEventPullRequestClosedTest() throws IOException {
        String repositoryBody = Resources.toString(
            Resources.getResource("client/event/GithubPullRequestClosedEvent.json"),
            StandardCharsets.UTF_8
        );
        WireMockServerExtension.getWireMockServer().stubFor(get(urlPathMatching("/repos/user/rep/events"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(repositoryBody)));

        GithubClient client = new GithubClient("http://localhost:" + WireMockServerExtension.getPort(), retryFilter);
        var result = client.getEvent("user", "rep");

        assertThat(result.createdAt()).isEqualTo(OffsetDateTime.parse("2024-03-17T20:20:37Z"));
        assertThat(result.getType()).isEqualTo(EventType.PULL_REQUEST);
        assertThat(result.getMessage()).isEqualTo("один из ПР в репозитории закрыт");
        assertThat(result.payload().getAction()).isEqualTo(Action.CLOSED);
    }

    @Test
    public void getEventPullRequestCommentTest() throws IOException {
        String repositoryBody = Resources.toString(
            Resources.getResource("client/event/GithubPullRequestCommentEvent.json"),
            StandardCharsets.UTF_8
        );
        WireMockServerExtension.getWireMockServer().stubFor(get(urlPathMatching("/repos/user/rep/events"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(repositoryBody)));

        GithubClient client = new GithubClient("http://localhost:" + WireMockServerExtension.getPort(), retryFilter);
        var result = client.getEvent("user", "rep");

        assertThat(result.createdAt()).isEqualTo(OffsetDateTime.parse("2024-03-14T09:42:21Z"));
        assertThat(result.getType()).isEqualTo(EventType.PULL_REQUEST_REVIEW_COMMENT);
        assertThat(result.getMessage()).isEqualTo("новый коментарий к коду в ПР");
    }

    @Test
    public void getEventUnknownTest() throws IOException {
        String repositoryBody = Resources.toString(
            Resources.getResource("client/event/GithubUnknownEvent.json"),
            StandardCharsets.UTF_8
        );
        WireMockServerExtension.getWireMockServer().stubFor(get(urlPathMatching("/repos/user/rep/events"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(repositoryBody)));

        GithubClient client = new GithubClient("http://localhost:" + WireMockServerExtension.getPort(), retryFilter);
        var result = client.getEvent("user", "rep");

        assertThat(result.getType()).isEqualTo(EventType.UNKNOWN);
        assertThat(result.getMessage()).isEqualTo("замечено новое обновление в репозитории");
    }

    @Test
    public void getEventEmptyTest() {
        String repositoryBody = "[]";
        WireMockServerExtension.getWireMockServer().stubFor(get(urlPathMatching("/repos/user/rep/events"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(repositoryBody)));

        GithubClient client = new GithubClient("http://localhost:" + WireMockServerExtension.getPort(), retryFilter);
        var result = client.getEvent("user", "rep");

        assertThat(result.getType()).isEqualTo(EventType.UNKNOWN);
        assertThat(result.getMessage()).isEqualTo("замечено новое обновление в репозитории");
    }

}
