package edu.java.bot.retry.impl;

import edu.java.bot.retry.BackoffPolicy;
import lombok.AllArgsConstructor;
import java.time.Duration;

@AllArgsConstructor
public class ConstantBackoff implements BackoffPolicy {
    private Duration waitTime;

    @Override
    public Duration getWaitTime(int attempt) {
        return waitTime;
    }
}
