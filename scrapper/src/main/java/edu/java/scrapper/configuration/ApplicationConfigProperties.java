package edu.java.scrapper.configuration;

import edu.java.scrapper.enums.RepositoryAccessType;
import edu.java.scrapper.retry.BackoffType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
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
    ClientProperties botProperties,

    @NotNull
    @DefaultValue("jdbc")
    @Name("database-access-type")
    RepositoryAccessType accessType
) {
    @Bean
    public Scheduler scheduler() {
        return scheduler;
    }

    @Bean
    public RepositoryAccessType implementation() {
        return accessType;
    }

    public record Scheduler(
        boolean enable,
        @NotNull Duration interval,
        @NotNull Duration oldLinkTime,
        @NotNull Duration forceCheckDelay
    ) {
    }

    public record ClientProperties(
        String baseUrl,
        BackoffConfig backoff
    ) {
        public record BackoffConfig(
            @NotNull
            @DefaultValue("constant")
            BackoffType type,
            @NotNull
            @PositiveOrZero
            @DefaultValue("0")
            int maxAttempts,
            @NotNull
            @DefaultValue("1s")
            Duration waitTime,
            @NotNull
            List<HttpStatus> codes
        ) {
        }
    }

}
