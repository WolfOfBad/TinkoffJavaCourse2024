package edu.java.scrapper.ratelimit;

import edu.java.scrapper.configuration.ApplicationConfigProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class RateLimitBucketsCache {
    private final Bandwidth bandwidth;

    public RateLimitBucketsCache(ApplicationConfigProperties.RateLimit config) {
        bandwidth = Bandwidth.classic(
            config.capacity(),
            Refill.greedy(
                config.refillRate(),
                config.refillTime()
            )
        );
    }

    @Cacheable(value = "rate-limit-buckets-cache", key = "#ip")
    public Bucket getBucket(String ip) {
        return buildBucket();
    }

    private Bucket buildBucket() {
        return Bucket.builder()
            .addLimit(bandwidth)
            .build();
    }

}
