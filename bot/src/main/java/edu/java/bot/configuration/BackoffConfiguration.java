package edu.java.bot.configuration;

import edu.java.bot.retry.BackoffPolicy;
import edu.java.bot.retry.impl.ConstantBackoff;
import edu.java.bot.retry.impl.ExponentialBackoff;
import edu.java.bot.retry.impl.LinearBackoff;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BackoffConfiguration {
    @Bean
    @ConditionalOnProperty(value = "app.backoff.type", havingValue = "constant")
    public BackoffPolicy constantBackoff(ApplicationConfigProperties.BackoffConfig config) {
        return new ConstantBackoff(config.waitTime());
    }

    @Bean
    @ConditionalOnProperty(value = "app.backoff.type", havingValue = "linear")
    public BackoffPolicy linearBackoff(ApplicationConfigProperties.BackoffConfig config) {
        return new LinearBackoff(
            config.waitTime()
        );
    }

    @Bean
    @ConditionalOnProperty(value = "app.backoff.type", havingValue = "exponential")
    public BackoffPolicy exponentialBackoff(ApplicationConfigProperties.BackoffConfig config) {
        return new ExponentialBackoff(
            config.waitTime()
        );
    }

}
