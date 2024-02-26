package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.github.GithubClient;
import edu.java.client.github.dto.RepositoryDto;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import wiremock.com.google.common.io.Resources;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;

public class GithubClientTest {
    private WireMockServer wireMockServer;
    private final int port = 8080;

    @BeforeEach
    public void before() {
        wireMockServer = new WireMockServer(port);
        wireMockServer.start();
    }

    @AfterEach
    public void after() {
        wireMockServer.stop();
    }

    @Test
    public void getRepositoryTest() throws IOException {
        String repositoryBody = Resources.toString(
            Resources.getResource("client/GithubRepositoryResponseBody.json"),
            StandardCharsets.UTF_8
        );
        wireMockServer.stubFor(get(urlPathMatching("/repos/user/rep"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(repositoryBody)));

        GithubClient client = new GithubClient("http://localhost:" + port);
        Mono<RepositoryDto> dtoMono = client.getRepository("user", "rep");
        RepositoryDto result = dtoMono.block();

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(751770237);
        assertThat(result.name()).isEqualTo("TinkoffJavaCourse2024");
        assertThat(result.pushedAt()).isEqualTo(OffsetDateTime.parse("2024-02-22T18:52:29Z"));
        assertThat(result.updatedAt()).isEqualTo(OffsetDateTime.parse("2024-02-02T09:41:40Z"));
        assertThat(result.owner().id()).isEqualTo(138949548);
        assertThat(result.owner().login()).isEqualTo("WolfOfBad");
    }

}
