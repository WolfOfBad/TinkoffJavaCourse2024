package edu.java.scrapper.configuration;

import edu.java.scrapper.service.linkchecker.GithubChecker;
import edu.java.scrapper.service.linkchecker.LinkCheckerManager;
import edu.java.scrapper.service.linkchecker.StackoverflowChecker;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ApplicationConfiguration {
    @Bean
    @Primary
    public LinkCheckerManager parserChain(
        GithubChecker githubChecker,
        StackoverflowChecker stackoverflowChecker
    ) {
        return LinkCheckerManager.link(
            githubChecker,
            stackoverflowChecker
        );
    }

    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }
}
