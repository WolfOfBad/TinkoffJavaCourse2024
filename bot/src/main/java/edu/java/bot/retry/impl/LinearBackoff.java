package edu.java.bot.retry.impl;

import edu.java.bot.configuration.ApplicationConfigProperties;
import edu.java.bot.retry.BackoffPolicy;
import java.time.Duration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LinearBackoff implements BackoffPolicy {
    private Duration waitTime;

    public LinearBackoff(ApplicationConfigProperties.BackoffConfig config) {
        this.waitTime = config.waitTime();
    }

    @Override
    public Duration getWaitTime(int attempt) {
        return waitTime.multipliedBy(attempt);
    }
}
