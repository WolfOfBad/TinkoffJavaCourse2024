package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.stackoverflow.StackoverflowClient;
import edu.java.client.stackoverflow.dto.QuestionDto;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import wiremock.com.google.common.io.Resources;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;

public class StackoverflowClientTest {
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
    public void getQuestionTest() throws IOException {
        String repositoryBody = Resources.toString(
            Resources.getResource("client/StackoverflowQuestionResponseBody.json"),
            StandardCharsets.UTF_8
        );
        wireMockServer.stubFor(get(urlPathMatching("/2.3/questions/11828270"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(repositoryBody)));

        StackoverflowClient client = new StackoverflowClient("http://localhost:" + port);
        Mono<QuestionDto> dtoMono = client.getQuestion(11828270L);
        QuestionDto result = dtoMono.block();

        assertThat(result).isNotNull();
        assertThat(result.getItems().getFirst().getQuestionId()).isEqualTo(11828270L);
        assertThat(result.getItems().getFirst().getLastActivity()).isEqualTo(OffsetDateTime.ofInstant(
            Instant.ofEpochSecond(1707143305),
            ZoneOffset.UTC
        ));
        assertThat(result.getItems().getFirst().getOwner().getId()).isEqualTo(1322662);
        assertThat(result.getItems().getFirst().getOwner().getName()).isEqualTo("jclancy");
    }

}
