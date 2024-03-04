package edu.java.scrapper.client.bot;

import edu.java.scrapper.client.bot.dto.request.LinkUpdateRequest;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {
    private final WebClient webClient;

    public BotClient(String baseUrl, WebClient.Builder builder) {
        webClient = builder.baseUrl(baseUrl).filter(apiErrorHandler()).build();
    }

    public void sendUpdate(long id, String uri, String description, List<Long> tgChatIds) {
        webClient.post().uri("/updates")
            .bodyValue(new LinkUpdateRequest(id, URI.create(uri), description, tgChatIds))
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
