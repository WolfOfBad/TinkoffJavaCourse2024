package edu.java.bot.client;

import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.reactive.function.client.WebClient;
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
    @BeforeEach
    public void before() {
        WireMockServerExtension.resetAll();
    }

    @Test
    public void registerTest() {
        WireMockServerExtension.getWireMockServer().stubFor(post(urlPathMatching("/tg-chat/1"))
            .willReturn(aResponse().withStatus(200)));

        ScrapperClient client = new ScrapperClient(
            "http://localhost:" + WireMockServerExtension.getPort(),
            WebClient.builder()
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
            WebClient.builder()
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
            WebClient.builder()
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
            WebClient.builder()
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
            WebClient.builder()
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
            WebClient.builder()
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
            WebClient.builder()
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
            WebClient.builder()
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
            WebClient.builder()
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
            WebClient.builder()
        );

        assertThrows(
            Exception.class,
            () -> client.deleteLink(1, "link")
        );
    }

}
