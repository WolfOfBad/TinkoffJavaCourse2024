package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@EnableScheduling
public record ApplicationConfigProperties(
    @NotNull
    Scheduler scheduler,

    @NotNull
    @Name("github-client")
    @DefaultValue("https://api.github.com")
    ClientProperties githubProperties,

    @NotNull
    @Name("stackoverflow-client")
    @DefaultValue("https://api.stackexchange.com")
    ClientProperties stackoverflowProperties,

    @NotNull
    @Name("bot-client")
    ClientProperties botProperties
) {
    @Bean
    public Duration intervalDelay() {
        return scheduler.interval();
    }

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record ClientProperties(String baseUrl) {
    }
}
