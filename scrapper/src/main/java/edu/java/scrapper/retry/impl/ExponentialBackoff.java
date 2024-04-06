package edu.java.scrapper.retry.impl;

import edu.java.scrapper.configuration.ApplicationConfigProperties;
import edu.java.scrapper.retry.BackoffPolicy;
import java.time.Duration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExponentialBackoff implements BackoffPolicy {
    private Duration waitTime;
    private static final int FACTOR = 2;

    public ExponentialBackoff(ApplicationConfigProperties.ClientProperties.BackoffConfig config) {
        this.waitTime = config.waitTime();
    }

    @Override
    public Duration getWaitTime(int attempt) {
        return waitTime.multipliedBy((long) Math.pow(FACTOR, attempt));
    }
}
