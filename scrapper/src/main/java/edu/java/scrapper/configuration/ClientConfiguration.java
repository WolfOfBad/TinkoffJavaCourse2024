package edu.java.scrapper.configuration;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.client.stackoverflow.StackoverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    @Bean
    public GithubClient githubClient(ApplicationConfigProperties properties) {
        return new GithubClient(properties.githubProperties().baseUrl());
    }

    @Bean
    public StackoverflowClient stackOverflowClient(ApplicationConfigProperties properties) {
        return new StackoverflowClient(properties.stackoverflowProperties().baseUrl());
    }

    @Bean
    public BotClient botClient(ApplicationConfigProperties properties, WebClient.Builder builder) {
        return new BotClient(properties.botProperties().baseUrl(), builder);
    }

}
