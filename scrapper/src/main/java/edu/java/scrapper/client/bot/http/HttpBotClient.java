package edu.java.scrapper.client.bot.http;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.bot.dto.request.LinkUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class HttpBotClient implements BotClient {
    private final WebClient webClient;

    public HttpBotClient(String baseUrl, ExchangeFilterFunction retryExchangeFilter) {
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .filter(apiErrorHandler())
            .filter(retryExchangeFilter)
            .build();
    }

    @Override
    public void send(LinkUpdateRequest linkUpdateRequest) {
        webClient.post().uri("/updates")
            .bodyValue(linkUpdateRequest)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    private static ExchangeFilterFunction apiErrorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST) {
                return clientResponse.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new Exception(errorBody)));
            } else {
                return Mono.just(clientResponse);
            }
        });
    }
}
