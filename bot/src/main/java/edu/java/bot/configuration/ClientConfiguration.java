package edu.java.bot.configuration;

import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.retry.RetryExchangeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public ScrapperClient scrapperClient(
        ApplicationConfigProperties properties,
        RetryExchangeFilter retryExchangeFilter
    ) {
        return new ScrapperClient(
            properties.scrapperUrl(),
            retryExchangeFilter
        );
    }

}
