package edu.java.scrapper.client.github;

import edu.java.scrapper.client.github.dto.RepositoryDto;
import edu.java.scrapper.client.github.dto.event.EventDTO;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

public class GithubClient {
    private final WebClient webClient;

    public GithubClient(String baseUrl, ExchangeFilterFunction retryExchangeFilter) {
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .filter(retryExchangeFilter)
            .build();
    }

    public RepositoryDto getRepository(String user, String repository) {
        return webClient.get().uri("/repos/{user}/{repository}", user, repository)
            .headers(defaultHeaders)
            .retrieve()
            .bodyToMono(RepositoryDto.class)
            .block();
    }

    public EventDTO getEvent(String user, String repository) {
        ResponseEntity<List<EventDTO>> events =
            webClient.get().uri("/repos/{user}/{repository}/events", user, repository)
                .headers(defaultHeaders)
                .header("per_page", "1")
                .retrieve()
                .toEntityList(EventDTO.class)
                .block();

        if (events != null && events.getBody() != null && !events.getBody().isEmpty()) {
            return events
                .getBody()
                .getFirst();
        }
        return EventDTO.getDefault();
    }

    private final Consumer<HttpHeaders> defaultHeaders = httpHeaders -> {
        httpHeaders.set("Accept", "application/vnd.github+json");
        httpHeaders.set("X-Github-Api-Version", "2022-11-28");
    };

}
