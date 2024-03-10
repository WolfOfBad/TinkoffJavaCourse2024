package edu.java.scrapper.client.stackoverflow;

import edu.java.scrapper.client.stackoverflow.dto.QuestionDto;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackoverflowClient {
    private final WebClient webClient;

    public StackoverflowClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public Mono<QuestionDto> getQuestion(long id) {
        return webClient.get()
            .uri("/2.3/questions/{id}?order=desc&sort=activity&site=stackoverflow", id)
            .retrieve()
            .bodyToMono(QuestionDto.class);
    }

}
