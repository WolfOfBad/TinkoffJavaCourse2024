package edu.java.scrapper.client;

import edu.java.scrapper.client.stackoverflow.StackoverflowClient;
import edu.java.scrapper.client.stackoverflow.dto.QuestionDto;
import edu.java.scrapper.configuration.ApplicationConfigProperties;
import edu.java.scrapper.retry.BackoffType;
import edu.java.scrapper.retry.RetryExchangeFilter;
import edu.java.scrapper.retry.impl.ConstantBackoff;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
public class StackoverflowClientTest {
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
    public void getQuestionTest() throws IOException {
        String repositoryBody = Resources.toString(
            Resources.getResource("client/StackoverflowQuestionResponseBody.json"),
            StandardCharsets.UTF_8
        );
        WireMockServerExtension.getWireMockServer().stubFor(get(urlPathMatching("/2.3/questions/11828270"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(repositoryBody)));

        StackoverflowClient client =
            new StackoverflowClient("http://localhost:" + WireMockServerExtension.getPort(), retryFilter);
        QuestionDto result = client.getQuestion(11828270L);

        assertThat(result).isNotNull();
        assertThat(result.items().getFirst().questionId()).isEqualTo(11828270L);
        assertThat(result.items().getFirst().lastActivity()).isEqualTo(OffsetDateTime.ofInstant(
            Instant.ofEpochSecond(1707143305),
            ZoneOffset.UTC
        ));
        assertThat(result.items().getFirst().owner().id()).isEqualTo(1322662);
        assertThat(result.items().getFirst().owner().name()).isEqualTo("jclancy");
    }

}
