package edu.java.scrapper.client;

import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.client.github.dto.RepositoryDto;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import wiremock.com.google.common.io.Resources;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(WireMockServerExtension.class)
public class GithubClientTest {
    @BeforeEach
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

        GithubClient client = new GithubClient("http://localhost:" + WireMockServerExtension.getPort());
        RepositoryDto result = client.getRepository("user", "rep");

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(751770237);
        assertThat(result.name()).isEqualTo("TinkoffJavaCourse2024");
        assertThat(result.pushedAt()).isEqualTo(OffsetDateTime.parse("2024-02-22T18:52:29Z"));
        assertThat(result.updatedAt()).isEqualTo(OffsetDateTime.parse("2024-02-02T09:41:40Z"));
        assertThat(result.owner().id()).isEqualTo(138949548);
        assertThat(result.owner().login()).isEqualTo("WolfOfBad");
    }

}
