package edu.java.scrapper.client.github;

import edu.java.scrapper.client.github.dto.RepositoryDto;
import java.util.function.Consumer;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

public class GithubClient {
    private final WebClient webClient;

    public GithubClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public RepositoryDto getRepository(String user, String repository) {
        return webClient.get().uri("/repos/{user}/{repository}", user, repository)
            .headers(defaultHeaders)
            .retrieve()
            .bodyToMono(RepositoryDto.class)
            .block();
    }

    private final Consumer<HttpHeaders> defaultHeaders = httpHeaders -> {
        httpHeaders.set("Accept", "application/vnd.github+json");
        httpHeaders.set("X-Github-Api-Version", "2022-11-28");
    };

}
