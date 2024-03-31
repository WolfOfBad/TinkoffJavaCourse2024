package edu.java.scrapper.client;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.configuration.ApplicationConfigProperties;
import edu.java.scrapper.retry.BackoffType;
import edu.java.scrapper.retry.RetryExchangeFilter;
import edu.java.scrapper.retry.impl.ConstantBackoff;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(WireMockServerExtension.class)
public class BotClientTest {
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
    public void sendUpdateTest() {
        WireMockServerExtension.getWireMockServer().stubFor(post(urlPathMatching("/updates"))
            .willReturn(aResponse().withStatus(200)));

        BotClient client = new BotClient(
            "http://localhost:" + WireMockServerExtension.getPort(),
            retryFilter
        );

        assertDoesNotThrow(() -> client.sendUpdate(1, "uri", "desc", List.of(1L)));
    }

    @Test
    public void sendUpdateFailTest() {
        WireMockServerExtension.getWireMockServer().stubFor(post(urlPathMatching("/updates"))
            .willReturn(aResponse()
                .withStatus(400)
                .withBody("""
                    {
                      "description": "string",
                      "code": "string",
                      "exceptionName": "string",
                      "exceptionMessage": "string",
                      "stacktrace": [
                        "string"
                      ]
                    }
                    """)));

        BotClient client = new BotClient(
            "http://localhost:" + WireMockServerExtension.getPort(),
            retryFilter
        );

        assertThrows(
            Exception.class,
            () -> client.sendUpdate(1, "uri", "desc", List.of(1L))
        );
    }
}
