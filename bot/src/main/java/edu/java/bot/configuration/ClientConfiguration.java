package edu.java.bot.configuration;

import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.service.SendMessageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

@Configuration
public class ClientConfiguration {
    @Bean
    public ScrapperClient scrapperClient(
        ApplicationConfigProperties properties,
        @Qualifier("retryExchangeFilter")
        ExchangeFilterFunction retryFilter,
        @Qualifier("scrapperExceptionHandler")
        ExchangeFilterFunction scrapperExceptionFilter
    ) {
        return new ScrapperClient(
            properties.scrapperUrl(),
            retryFilter,
            scrapperExceptionFilter
        );
    }

}
