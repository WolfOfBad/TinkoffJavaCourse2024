package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.jdbc.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.JdbcLinkRepository;
import edu.java.scrapper.domain.jooq.JooqChatRepository;
import edu.java.scrapper.domain.jooq.JooqLinkRepository;
import edu.java.scrapper.enums.RepositoryAccessType;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class RepositoryAccessConfiguration {
    @Bean
    @Primary
    public ChatRepository chatRepository(
        RepositoryAccessType accessType,
        JdbcClient jdbcClient,
        DSLContext dslContext
    ) {
        return switch (accessType) {
            case JDBC -> new JdbcChatRepository(jdbcClient);
            case JOOQ -> new JooqChatRepository(dslContext);
        };
    }

    @Bean
    @Primary
    public LinkRepository linkRepository(
        RepositoryAccessType accessType,
        JdbcClient jdbcClient,
        DSLContext dslContext
    ) {
        return switch (accessType) {
            case JDBC -> new JdbcLinkRepository(jdbcClient);
            case JOOQ -> new JooqLinkRepository(dslContext);
        };
    }
}
