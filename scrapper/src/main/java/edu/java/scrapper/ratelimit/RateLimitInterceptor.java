package edu.java.scrapper.ratelimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

@AllArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {
    private RateLimitBucketsCache cache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
        IOException {
        Bucket tokenBucket = cache.getBucket(request.getRemoteAddr());
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            long remainingTokens = probe.getRemainingTokens();

            response.addHeader(
                "X-Rate-Limit-Remaining",
                String.valueOf(remainingTokens)
            );

            return true;
        }

        Duration waitDuration = Duration.ofNanos(
            probe.getNanosToWaitForRefill()
        );

        response.addHeader(
            "X-Rate-Limit-Retry-After-Seconds",
            String.valueOf(waitDuration.toSeconds())
        );

        response.sendError(
            HttpStatus.TOO_MANY_REQUESTS.value(),
            "You have exhausted your API request quota"
        );

        return false;
    }

}
