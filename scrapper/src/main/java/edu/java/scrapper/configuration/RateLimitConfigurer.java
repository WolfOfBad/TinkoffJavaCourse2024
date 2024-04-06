package edu.java.scrapper.configuration;

import edu.java.scrapper.ratelimit.RateLimitBucketsCache;
import edu.java.scrapper.ratelimit.RateLimitInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class RateLimitConfigurer implements WebMvcConfigurer {
    private final RateLimitBucketsCache rateLimitBucketsCache;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
            .addInterceptor(new RateLimitInterceptor(rateLimitBucketsCache))
            .addPathPatterns("/**");
    }
}
