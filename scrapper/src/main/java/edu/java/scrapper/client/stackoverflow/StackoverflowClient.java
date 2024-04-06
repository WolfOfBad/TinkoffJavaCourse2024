package edu.java.scrapper.client.stackoverflow;

import edu.java.scrapper.client.stackoverflow.dto.QuestionDto;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

public class StackoverflowClient {
    private final WebClient webClient;

    public StackoverflowClient(String baseUrl, ExchangeFilterFunction retryExchangeFilter) {
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .filter(retryExchangeFilter)
            .build();
    }

    public QuestionDto getQuestion(long id) {
        return webClient.get()
            .uri("/2.3/questions/{id}?order=desc&sort=activity&site=stackoverflow", id)
            .retrieve()
            .bodyToMono(QuestionDto.class)
            .block();
    }

}
