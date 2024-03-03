package edu.java.bot.configuration;

import edu.java.bot.client.scrapper.ScrapperClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    @Bean
    public ScrapperClient scrapperClient(ApplicationConfigProperties properties, WebClient.Builder builder) {
        return new ScrapperClient(properties.scrapperUrl(), builder);
    }

}
