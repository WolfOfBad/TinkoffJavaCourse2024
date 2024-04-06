package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.retry.BackoffType;
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
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfigProperties(
    @NotEmpty
    String telegramToken,

    @NotEmpty
    String scrapperUrl,

    @NotNull
    @Name("backoff")
    BackoffConfig backoff
) {
    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(telegramToken);
    }

    @Bean
    public BackoffConfig backoffConfig() {
        return backoff;
    }

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
