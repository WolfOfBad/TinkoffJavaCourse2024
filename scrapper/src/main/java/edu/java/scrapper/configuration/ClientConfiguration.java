package edu.java.scrapper.configuration;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.client.stackoverflow.StackoverflowClient;
import edu.java.scrapper.retry.BackoffPolicy;
import edu.java.scrapper.retry.RetryExchangeFilter;
import edu.java.scrapper.retry.impl.ConstantBackoff;
import edu.java.scrapper.retry.impl.ExponentialBackoff;
import edu.java.scrapper.retry.impl.LinearBackoff;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public GithubClient githubClient(ApplicationConfigProperties properties) {
        ApplicationConfigProperties.ClientProperties config = properties.githubProperties();
        return new GithubClient(
            config.baseUrl(),
            new RetryExchangeFilter(
                getPolicy(config.backoff()),
                config.backoff().codes(),
                config.backoff().maxAttempts()
            )
        );
    }

    @Bean
    public StackoverflowClient stackOverflowClient(ApplicationConfigProperties properties) {
        ApplicationConfigProperties.ClientProperties config = properties.stackoverflowProperties();
        return new StackoverflowClient(
            config.baseUrl(),
            new RetryExchangeFilter(
                getPolicy(config.backoff()),
                config.backoff().codes(),
                config.backoff().maxAttempts()
            )
        );
    }

    @Bean
    public BotClient botClient(ApplicationConfigProperties properties) {
        ApplicationConfigProperties.ClientProperties config = properties.botProperties();
        return new BotClient(
            config.baseUrl(),
            new RetryExchangeFilter(
                getPolicy(config.backoff()),
                config.backoff().codes(),
                config.backoff().maxAttempts()
            )
        );
    }

    private BackoffPolicy getPolicy(ApplicationConfigProperties.ClientProperties.BackoffConfig config) {
        return switch (config.type()) {
            case CONSTANT -> new ConstantBackoff(config.waitTime());
            case LINEAR -> new LinearBackoff(config.waitTime());
            case EXPONENTIAL -> new ExponentialBackoff(config.waitTime());
        };
    }

}
