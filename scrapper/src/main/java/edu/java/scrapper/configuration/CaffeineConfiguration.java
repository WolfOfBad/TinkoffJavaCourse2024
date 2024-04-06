package edu.java.scrapper.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineConfiguration {
    @Bean
    public Caffeine caffeineConfig(ApplicationConfigProperties.RateLimit config) {
        return Caffeine.newBuilder()
            .expireAfterAccess(config.expireTime())
            .maximumSize(config.cacheSize());
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCaffeine(caffeine);
        return manager;
    }
}
