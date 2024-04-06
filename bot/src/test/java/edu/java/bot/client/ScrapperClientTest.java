package edu.java.bot.client;

import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import edu.java.bot.configuration.ApplicationConfigProperties;
import edu.java.bot.retry.BackoffType;
import edu.java.bot.retry.RetryExchangeFilter;
import edu.java.bot.retry.impl.ConstantBackoff;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(WireMockServerExtension.class)
public class ScrapperClientTest {
    private ExchangeFilterFunction retryFilter = new RetryExchangeFilter(
        new ConstantBackoff(Duration.ZERO),
        new ApplicationConfigProperties.BackoffConfig(
            BackoffType.CONSTANT,
            2,
            Duration.ZERO,
            new ArrayList<>()
        )
    );

    @AfterEach
    public void after() {
        WireMockServerExtension.resetAll();
    }

    @Test
    public void registerTest() {
        WireMockServerExtension.getWireMockServer().stubFor(post(urlPathMatching("/tg-chat/1"))
            .willReturn(aResponse().withStatus(200)));

        ScrapperClient client = new ScrapperClient(
            "http://localhost:" + WireMockServerExtension.getPort(),
            retryFilter
        );

        assertDoesNotThrow(() -> client.registerChat(1));
    }

    @Test
    public void registerFailTest() {
        WireMockServerExtension.getWireMockServer().stubFor(post(urlPathMatching("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(400)
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

        ScrapperClient client = new ScrapperClient(
            "http://localhost:" + WireMockServerExtension.getPort(),
            retryFilter
        );

        assertThrows(
            Exception.class,
            () -> client.registerChat(1)
        );
    }

    @Test
    public void deleteTest() {
        WireMockServerExtension.getWireMockServer().stubFor(delete(urlPathMatching("/tg-chat/1"))
            .willReturn(aResponse().withStatus(200)));

        ScrapperClient client = new ScrapperClient(
            "http://localhost:" + WireMockServerExtension.getPort(),
            retryFilter
        );

        assertDoesNotThrow(() -> client.deleteChat(1));
    }

    @Test
    public void deleteFailTest() {
        WireMockServerExtension.getWireMockServer().stubFor(delete(urlPathMatching("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(400)
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

        ScrapperClient client = new ScrapperClient(
            "http://localhost:" + WireMockServerExtension.getPort(),
            retryFilter
        );

        assertThrows(
            Exception.class,
            () -> client.deleteChat(1)
        );
    }

    @Test
    public void getLinksTest() {
        WireMockServerExtension.getWireMockServer()
            .stubFor(get(urlPathMatching("/links"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                          "links": [
                            {
                              "id": 0,
                              "url": "string"
                            }
                          ],
                          "size": 1
                        }
                        """)));

        ScrapperClient client = new ScrapperClient(
            "http://localhost:" + WireMockServerExtension.getPort(),
            retryFilter
        );

        ListLinksResponse response = client.getTrackedLinks(1);

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.links().getFirst().id()).isEqualTo(0);
        assertThat(response.links().getFirst().uri()).isEqualTo(URI.create("string"));
    }

    @Test
    public void getLinksFailTest() {
        WireMockServerExtension.getWireMockServer().stubFor(get(urlPathMatching("/links"))
            .willReturn(aResponse()
                .withStatus(400)
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

        ScrapperClient client = new ScrapperClient(
            "http://localhost:" + WireMockServerExtension.getPort(),
            retryFilter
        );

        assertThrows(
            Exception.class,
            () -> client.registerChat(1)
        );
    }

    @Test
    public void addLinkTest() {
        WireMockServerExtension.getWireMockServer()
            .stubFor(post(urlPathMatching("/links"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                            {
                              "id": 0,
                              "url": "string"
                            }
                        """)));

        ScrapperClient client = new ScrapperClient(
            "http://localhost:" + WireMockServerExtension.getPort(),
            retryFilter
        );

        LinkResponse response = client.addLink(1, "link");

        assertThat(response.uri()).isEqualTo(URI.create("string"));
        assertThat(response.id()).isEqualTo(0);
    }

    @Test
    public void addLinkFailTest() {
        WireMockServerExtension.getWireMockServer().stubFor(post(urlPathMatching("/links"))
            .willReturn(aResponse()
                .withStatus(400)
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

        ScrapperClient client = new ScrapperClient(
            "http://localhost:" + WireMockServerExtension.getPort(),
            retryFilter
        );

        assertThrows(
            Exception.class,
            () -> client.addLink(1, "link")
        );
    }

    @Test
    public void deleteLinkTest() {
        WireMockServerExtension.getWireMockServer()
            .stubFor(delete(urlPathMatching("/links"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                            {
                              "id": 0,
                              "url": "string"
                            }
                        """)));

        ScrapperClient client = new ScrapperClient(
            "http://localhost:" + WireMockServerExtension.getPort(),
            retryFilter
        );

        LinkResponse response = client.deleteLink(1, "link");

        assertThat(response.uri()).isEqualTo(URI.create("string"));
        assertThat(response.id()).isEqualTo(0);
    }

    @Test
    public void deleteLinkFailTest() {
        WireMockServerExtension.getWireMockServer().stubFor(post(urlPathMatching("/links"))
            .willReturn(aResponse()
                .withStatus(400)
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

        ScrapperClient client = new ScrapperClient(
            "http://localhost:" + WireMockServerExtension.getPort(),
            retryFilter
        );

        assertThrows(
            Exception.class,
            () -> client.deleteLink(1, "link")
        );
    }

}
