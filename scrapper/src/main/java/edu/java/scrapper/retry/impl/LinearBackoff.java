package edu.java.scrapper.retry.impl;

import edu.java.scrapper.configuration.ApplicationConfigProperties;
import edu.java.scrapper.retry.BackoffPolicy;
import java.time.Duration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LinearBackoff implements BackoffPolicy {
    private Duration waitTime;

    public LinearBackoff(ApplicationConfigProperties.ClientProperties.BackoffConfig config) {
        this.waitTime = config.waitTime();
    }

    @Override
    public Duration getWaitTime(int attempt) {
        return waitTime.multipliedBy(attempt);
    }
}
