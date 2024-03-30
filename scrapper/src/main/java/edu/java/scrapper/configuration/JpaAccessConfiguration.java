package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.TgChatService;
import edu.java.scrapper.service.UpdateNotifier;
import edu.java.scrapper.service.impl.JpaLinkService;
import edu.java.scrapper.service.impl.JpaLinkUpdater;
import edu.java.scrapper.service.impl.JpaTgChatService;
import edu.java.scrapper.service.linkchecker.LinkCheckerManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
@EnableJpaRepositories(basePackages = "edu.java.scrapper")
public class JpaAccessConfiguration {
    @Bean
    public LinkUpdater linkUpdater(
        LinkCheckerManager linkCheckerManager,
        UpdateNotifier notifier,
        JpaLinkRepository linkRepository
    ) {
        return new JpaLinkUpdater(linkCheckerManager, linkRepository, notifier);
    }

    @Bean
    public LinkService linkService(
        JpaChatRepository chatRepository,
        JpaLinkRepository linkRepository,
        LinkUpdater linkUpdater
    ) {
        return new JpaLinkService(chatRepository, linkRepository, linkUpdater);
    }

    @Bean
    public TgChatService chatService(JpaChatRepository chatRepository, JpaLinkRepository linkRepository) {
        return new JpaTgChatService(chatRepository, linkRepository);
    }

}
