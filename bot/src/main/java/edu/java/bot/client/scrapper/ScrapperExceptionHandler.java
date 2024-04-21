package edu.java.bot.client.scrapper;

import edu.java.bot.controller.dto.response.ApiErrorResponse;
import edu.java.bot.exception.scrapper.AlreadySubscribedException;
import edu.java.bot.exception.scrapper.NoSuchChatException;
import edu.java.bot.exception.scrapper.NoSuchLinkException;
import edu.java.bot.exception.scrapper.UserAlreadyRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Service("scrapperExceptionHandler")
public class ScrapperExceptionHandler implements ExchangeFilterFunction {
    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request)
            .flatMap(clientResponse -> {
                HttpStatus code = (HttpStatus) clientResponse.statusCode();

                if (code == HttpStatus.UNAUTHORIZED) {
                    return Mono.error(new NoSuchChatException());
                } else if (code == HttpStatus.NOT_FOUND) {
                    return Mono.error(new NoSuchLinkException());
                } else if (code == HttpStatus.CONFLICT) {
                    return clientResponse.bodyToMono(ApiErrorResponse.class)
                        .flatMap(response -> {
                            if (response.description().contains("Already subscribed exception")) {
                                return Mono.error(new AlreadySubscribedException());
                            }
                            return Mono.error(new UserAlreadyRegisteredException());
                        });
                }

                return Mono.just(clientResponse);
            });
    }
}
