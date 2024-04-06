package edu.java.scrapper.retry;

import edu.java.scrapper.configuration.ApplicationConfigProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class RetryExchangeFilter implements ExchangeFilterFunction {
    private BackoffPolicy backoffPolicy;
    private List<HttpStatus> httpStatuses;
    private int maxAttempts;

    public RetryExchangeFilter(
        BackoffPolicy backoffPolicy,
        ApplicationConfigProperties.ClientProperties.BackoffConfig config
    ) {
        this.backoffPolicy = backoffPolicy;
        this.httpStatuses = config.codes();
        this.maxAttempts = config.maxAttempts();
    }

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return retry(request, next, 1);
    }

    private Mono<ClientResponse> retry(ClientRequest request, ExchangeFunction next, int attempt) {
        return next
            .exchange(request)
            .flatMap(clientResponse -> {
                HttpStatus code = (HttpStatus) clientResponse.statusCode();

                if (httpStatuses.contains(code) && attempt <= maxAttempts) {
                    return Mono.delay(backoffPolicy.getWaitTime(attempt))
                        .then(Mono.defer(() -> retry(request, next, attempt + 1)));
                } else if (attempt > maxAttempts) {
                    return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new Exception(
                            "Failed to execute client method " + maxAttempts + " times. Error: " + errorBody
                        )));
                }

                return Mono.just(clientResponse);
            });
    }
}
