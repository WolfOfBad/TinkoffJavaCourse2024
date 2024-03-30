package edu.java.scrapper.service.jpa;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.jpa.entity.ChatEntity;
import edu.java.scrapper.domain.jpa.entity.LinkEntity;
import edu.java.scrapper.exception.AlreadySubscribedException;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.exception.NoSuchLinkException;
import edu.java.scrapper.integration.IntegrationTest;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.TgChatService;
import edu.java.scrapper.service.impl.JpaLinkService;
import java.net.URI;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestPropertySource(properties = {"app.database-access-type=jpa"})
@ExtendWith(MockitoExtension.class)
public class JpaLinkServiceTest extends IntegrationTest {
    private LinkService linkService;

    @Autowired
    private TgChatService chatService;

    @Autowired
    private JpaLinkRepository linkRepository;

    @Autowired
    private JpaChatRepository chatRepository;

    @Mock
    private LinkUpdater linkUpdater;

    @BeforeEach
    public void before() {
        linkService = new JpaLinkService(chatRepository, linkRepository, linkUpdater);
    }

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        long tgChatId = 123;
        chatService.register(tgChatId);

        Optional<LinkEntity> before = linkRepository.findByUri(URI.create("test"));
        linkService.add(tgChatId, URI.create("test"));
        Optional<LinkEntity> after = linkRepository.findByUri(URI.create("test"));
        Optional<ChatEntity> chat = chatRepository.findByTgChatId(tgChatId);

        assertThat(before).isEmpty();
        assertThat(after).isPresent();
        assertThat(chat.get().getLinks()).containsExactly(after.get());
    }

    @Test
    @Transactional
    @Rollback
    public void addNotRegisteredTest() {
        assertThatThrownBy(() -> linkService.add(123, URI.create("test")))
            .isInstanceOf(NoSuchChatException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void addAlreadySubscribedTest() {
        chatService.register(123);
        linkService.add(123, URI.create("test"));

        assertThatThrownBy(() -> linkService.add(123, URI.create("test")))
            .isInstanceOf(AlreadySubscribedException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void removeTest() {
        chatService.register(123);
        linkService.add(123, URI.create("test"));

        Optional<LinkEntity> before = linkRepository.findByUri(URI.create("test"));
        linkService.remove(123, URI.create("test"));
        Optional<LinkEntity> after = linkRepository.findByUri(URI.create("test"));

        assertThat(before).isPresent();
        assertThat(after).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void removeUnregisteredTest() {
        assertThatThrownBy(() -> linkService.remove(123, URI.create("test")))
            .isInstanceOf(NoSuchChatException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void removeNotExistTest() {
        chatService.register(123);

        assertThatThrownBy(() -> linkService.remove(123, URI.create("test")))
            .isInstanceOf(NoSuchLinkException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void allLinksTest() {
        chatService.register(123);

        linkService.add(123, URI.create("test1"));
        linkService.add(123, URI.create("test2"));

        var result = linkService.allLinks(123);
        LinkEntity link1 = linkRepository.findByUri(URI.create("test1")).get();
        LinkEntity link2 = linkRepository.findByUri(URI.create("test2")).get();

        Link resultLink1 = new Link(link1.getId(), link1.getUri(), link1.getLastUpdate());
        Link resultLink2 = new Link(link2.getId(), link2.getUri(), link2.getLastUpdate());

        assertThat(result).containsExactly(resultLink1, resultLink2);
    }

}

