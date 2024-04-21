package edu.java.bot.client.scrapper;

import edu.java.bot.client.scrapper.dto.request.AddLinkRequest;
import edu.java.bot.client.scrapper.dto.request.RemoveLinkRequest;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import edu.java.bot.exception.scrapper.ScrapperException;
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

    public ScrapperClient(
        String baseUrl,
        ExchangeFilterFunction retryFilter,
        ExchangeFilterFunction scrapperExceptionFilter
    ) {
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .filter(scrapperExceptionFilter
                .andThen(retryFilter)
                .andThen(apiErrorHandler()))
            .build();
    }

    public void registerChat(long id) throws ScrapperException {
        webClient.post().uri(CHAT_PATH, id)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(ScrapperException.class, e -> {
                throw e;
            })
            .block();
    }

    public void deleteChat(long id) throws ScrapperException {
        webClient.delete().uri(CHAT_PATH, id)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(ScrapperException.class, e -> {
                throw e;
            })
            .block();
    }

    public ListLinksResponse getTrackedLinks(long chatId) throws ScrapperException {
        return webClient.get().uri(LINKS_PATH)
            .header(TG_CHAT_ID_HEADER, String.valueOf(chatId))
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .doOnError(ScrapperException.class, e -> {
                throw e;
            })
            .block();
    }

    public LinkResponse addLink(long chatId, String link) throws ScrapperException {
        return webClient.post().uri(LINKS_PATH)
            .header(TG_CHAT_ID_HEADER, String.valueOf(chatId))
            .bodyValue(new AddLinkRequest(URI.create(link)))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .doOnError(ScrapperException.class, e -> {
                throw e;
            })
            .block();
    }

    public LinkResponse deleteLink(long chatId, String link) throws ScrapperException {
        return webClient.method(HttpMethod.DELETE).uri(LINKS_PATH)
            .header(TG_CHAT_ID_HEADER, String.valueOf(chatId))
            .bodyValue(new RemoveLinkRequest(URI.create(link)))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .doOnError(ScrapperException.class, e -> {
                throw e;
            })
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
