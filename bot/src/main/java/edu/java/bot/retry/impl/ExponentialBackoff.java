package edu.java.bot.retry.impl;

import edu.java.bot.configuration.ApplicationConfigProperties;
import edu.java.bot.retry.BackoffPolicy;
import java.time.Duration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExponentialBackoff implements BackoffPolicy {
    private Duration waitTime;
    private static final int FACTOR = 2;

    public ExponentialBackoff(ApplicationConfigProperties.BackoffConfig config) {
        this.waitTime = config.waitTime();
    }

    @Override
    public Duration getWaitTime(int attempt) {
        return waitTime.multipliedBy((long) Math.pow(FACTOR, attempt));
    }
}
