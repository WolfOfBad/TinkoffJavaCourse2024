package edu.java.scrapper.retry;

import edu.java.scrapper.client.WireMockServerExtension;
import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.configuration.ApplicationConfigProperties;
import edu.java.scrapper.retry.impl.ConstantBackoff;
import edu.java.scrapper.retry.impl.ExponentialBackoff;
import edu.java.scrapper.retry.impl.LinearBackoff;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(WireMockServerExtension.class)
public class RetryExchangeFilterTest {

    @BeforeEach
    public void before() throws IOException {


        WireMockServerExtension.getWireMockServer().stubFor(get(urlPathMatching("/repos/user/rep/events"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", "application/json")
                .withBody("body")));
    }

    @AfterEach
    public void after() {
        WireMockServerExtension.resetAll();
    }

    @ParameterizedTest
    @MethodSource("args")
    public void retryTest(BackoffType type, BackoffPolicy policy) {
        ApplicationConfigProperties.ClientProperties.BackoffConfig config =
            new ApplicationConfigProperties.ClientProperties.BackoffConfig(
                type,
                5,
                Duration.ofSeconds(1),
                List.of(HttpStatus.BAD_REQUEST)
            );
        RetryExchangeFilter retryExchangeFilter = new RetryExchangeFilter(
            policy,
            config
        );

        GithubClient client = new GithubClient(
            "http://localhost:" + WireMockServerExtension.getPort(),
            retryExchangeFilter
        );

        assertThatThrownBy(() -> client.getEvent("user", "rep"))
            .message()
            .contains("Failed to execute client method 5 times. Error: ");
    }

    public static Arguments[] args() {
        return new Arguments[] {
            Arguments.of(
                BackoffType.CONSTANT,
                new ConstantBackoff(new ApplicationConfigProperties.ClientProperties.BackoffConfig(
                    BackoffType.CONSTANT,
                    5,
                    Duration.ofMillis(100),
                    List.of(HttpStatus.BAD_REQUEST)
                ))
            ),
            Arguments.of(
                BackoffType.LINEAR,
                new LinearBackoff(new ApplicationConfigProperties.ClientProperties.BackoffConfig(
                    BackoffType.LINEAR,
                    5,
                    Duration.ofMillis(100),
                    List.of(HttpStatus.BAD_REQUEST)
                ))
            ),
            Arguments.of(
                BackoffType.EXPONENTIAL,
                new ExponentialBackoff(new ApplicationConfigProperties.ClientProperties.BackoffConfig(
                    BackoffType.EXPONENTIAL,
                    5,
                    Duration.ofMillis(100),
                    List.of(HttpStatus.BAD_REQUEST)
                ))
            )
        };
    }
}
