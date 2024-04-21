package edu.java.bot.retry;

import edu.java.bot.client.WireMockServerExtension;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.client.scrapper.ScrapperExceptionHandler;
import edu.java.bot.configuration.ApplicationConfigProperties;
import edu.java.bot.retry.impl.ConstantBackoff;
import edu.java.bot.retry.impl.ExponentialBackoff;
import edu.java.bot.retry.impl.LinearBackoff;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith({WireMockServerExtension.class, MockitoExtension.class})
public class RetryExchangeFilterTest {
    @Mock
    private ScrapperExceptionHandler scrapperExceptionHandler;


    @BeforeEach
    public void before() {
        WireMockServerExtension.getWireMockServer().stubFor(post(urlPathMatching("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(501)
                .withHeader("Content-Type", "application/json")
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
    }

    @AfterEach
    public void after() {
        WireMockServerExtension.resetAll();
    }

    @ParameterizedTest
    @MethodSource("args")
    public void retryTest(BackoffType type, BackoffPolicy policy) {
        ApplicationConfigProperties.BackoffConfig config = new ApplicationConfigProperties.BackoffConfig(
            type,
            5,
            Duration.ofSeconds(1),
            List.of(HttpStatus.NOT_IMPLEMENTED)
        );
        RetryExchangeFilter retryExchangeFilter = new RetryExchangeFilter(
            policy,
            config
        );

        ScrapperClient client = new ScrapperClient(
            "http://localhost:" + WireMockServerExtension.getPort(),
            retryExchangeFilter,
            new ScrapperExceptionHandler()
        );

        assertThatThrownBy(() -> client.registerChat(1))
            .message()
            .contains("Failed to execute client method 5 times. Error: ");
    }

    public static Arguments[] args() {
        return new Arguments[] {
            Arguments.of(BackoffType.CONSTANT, new ConstantBackoff(new ApplicationConfigProperties.BackoffConfig(
                BackoffType.CONSTANT,
                5,
                Duration.ofMillis(100),
                List.of(HttpStatus.BAD_REQUEST)
            ))),
            Arguments.of(BackoffType.LINEAR, new LinearBackoff(new ApplicationConfigProperties.BackoffConfig(
                BackoffType.LINEAR,
                5,
                Duration.ofMillis(100),
                List.of(HttpStatus.BAD_REQUEST)
            ))),
            Arguments.of(BackoffType.EXPONENTIAL, new ExponentialBackoff(new ApplicationConfigProperties.BackoffConfig(
                BackoffType.EXPONENTIAL,
                5,
                Duration.ofMillis(100),
                List.of(HttpStatus.BAD_REQUEST)
            )))
        };
    }
}
