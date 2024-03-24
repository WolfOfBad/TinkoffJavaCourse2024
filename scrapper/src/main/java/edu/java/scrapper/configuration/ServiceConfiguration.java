package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.enums.RepositoryAccessType;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.TgChatService;
import edu.java.scrapper.service.UpdateNotifier;
import edu.java.scrapper.service.impl.DefaultLinkService;
import edu.java.scrapper.service.impl.DefaultLinkUpdater;
import edu.java.scrapper.service.impl.DefaultTgChatService;
import edu.java.scrapper.service.linkchecker.LinkCheckerManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ServiceConfiguration {
    @Bean
    @Primary
    public LinkService linkService(
        RepositoryAccessType accessType,
        LinkRepository repository,
        LinkUpdater updater
    ) {
        return switch (accessType) {
            case JDBC -> new DefaultLinkService(repository, updater);
        };
    }

    @Bean
    @Primary
    public LinkUpdater linkUpdater(
        RepositoryAccessType accessType,
        LinkCheckerManager checker,
        LinkRepository repository,
        UpdateNotifier notifier
    ) {
        return switch (accessType) {
            case JDBC -> new DefaultLinkUpdater(checker, repository, notifier);
        };
    }

    @Bean
    @Primary
    public TgChatService tgChatService(
        RepositoryAccessType accessType,
        ChatRepository chatRepository,
        LinkRepository linkRepository
    ) {
        return switch (accessType) {
            case JDBC -> new DefaultTgChatService(chatRepository, linkRepository);
        };
    }
}
