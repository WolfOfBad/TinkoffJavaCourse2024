package edu.java.configuration;

import edu.java.client.github.GithubClient;
import edu.java.client.stackoverflow.StackoverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
