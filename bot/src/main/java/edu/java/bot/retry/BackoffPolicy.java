package edu.java.bot.retry;

import java.time.Duration;

@FunctionalInterface
public interface BackoffPolicy {
    Duration getWaitTime(int attempt);
}
