package edu.java.bot.client.scrapper;

import edu.java.bot.client.scrapper.dto.request.AddLinkRequest;
import edu.java.bot.client.scrapper.dto.request.RemoveLinkRequest;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import java.net.URI;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    private final WebClient webClient;
    private static final String CHAT_PATH = "tg-chat/{id}";
    private static final String LINKS_PATH = "/links";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    public ScrapperClient(String baseUrl, WebClient.Builder builder, ExchangeFilterFunction retryFilter) {
        webClient = builder.baseUrl(baseUrl)
            .filter(apiErrorHandler())
            .filter(retryFilter)
            .build();
    }

    public void registerChat(long id) {
        webClient.post().uri(CHAT_PATH, id)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public void deleteChat(long id) {
        webClient.delete().uri(CHAT_PATH, id)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public ListLinksResponse getTrackedLinks(long chatId) {
        return webClient.get().uri(LINKS_PATH)
            .header(TG_CHAT_ID_HEADER, String.valueOf(chatId))
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(long chatId, String link) {
        return webClient.post().uri(LINKS_PATH)
            .header(TG_CHAT_ID_HEADER, String.valueOf(chatId))
            .bodyValue(new AddLinkRequest(URI.create(link)))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse deleteLink(long chatId, String link) {
        return webClient.method(HttpMethod.DELETE).uri(LINKS_PATH)
            .header(TG_CHAT_ID_HEADER, String.valueOf(chatId))
            .bodyValue(new RemoveLinkRequest(URI.create(link)))
            .retrieve()
            .bodyToMono(LinkResponse.class)
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
