package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.jooq.JooqChatRepository;
import edu.java.scrapper.domain.jooq.JooqLinkRepository;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.TgChatService;
import edu.java.scrapper.service.UpdateNotifier;
import edu.java.scrapper.service.impl.DefaultLinkService;
import edu.java.scrapper.service.impl.DefaultLinkUpdater;
import edu.java.scrapper.service.impl.DefaultTgChatService;
import edu.java.scrapper.service.linkchecker.LinkCheckerManager;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public ChatRepository chatRepository(DSLContext context) {
        return new JooqChatRepository(context);
    }

    @Bean
    public LinkRepository linkRepository(DSLContext context) {
        return new JooqLinkRepository(context);
    }

    @Bean
    public LinkUpdater linkUpdater(
        LinkCheckerManager linkChecker,
        LinkRepository linkRepository,
        UpdateNotifier notifier
    ) {
        return new DefaultLinkUpdater(linkChecker, linkRepository, notifier);
    }

    @Bean
    public LinkService linkService(
        LinkRepository linkRepository,
        LinkUpdater linkUpdater
    ) {
        return new DefaultLinkService(linkRepository, linkUpdater);
    }

    @Bean
    public TgChatService chatService(
        LinkRepository linkRepository,
        ChatRepository chatRepository
    ) {
        return new DefaultTgChatService(chatRepository, linkRepository);
    }
}
