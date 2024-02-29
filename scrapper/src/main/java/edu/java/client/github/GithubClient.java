package edu.java.client.github;

import edu.java.client.github.dto.RepositoryDto;
import java.util.function.Consumer;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GithubClient {
    private final WebClient webClient;

    public GithubClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public Mono<RepositoryDto> getRepository(String user, String repository) {
        return webClient.get().uri("/repos/{user}/{repository}", user, repository)
            .headers(defaultHeaders)
            .retrieve()
            .bodyToMono(RepositoryDto.class);
    }

    private final Consumer<HttpHeaders> defaultHeaders = httpHeaders -> {
        httpHeaders.set("Accept", "application/vnd.github+json");
        httpHeaders.set("X-Github-Api-Version", "2022-11-28");
    };

}