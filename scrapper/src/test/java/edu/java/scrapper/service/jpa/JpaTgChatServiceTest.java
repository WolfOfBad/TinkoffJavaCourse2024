package edu.java.scrapper.service.jpa;

import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.jpa.entity.ChatEntity;
import edu.java.scrapper.domain.jpa.entity.LinkEntity;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.exception.UserAlreadyRegisteredException;
import edu.java.scrapper.integration.IntegrationTest;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.TgChatService;
import java.net.URI;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestPropertySource(properties = {"app.database-access-type=jpa", "app.scheduler.enable=false"})
public class JpaTgChatServiceTest extends IntegrationTest {
    @Autowired
    private TgChatService chatService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private JpaChatRepository chatRepository;

    @Autowired
    private JpaLinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        long tgChatId = 123;

        chatService.register(tgChatId);

        Optional<ChatEntity> chat = chatRepository.getByTgChatId(tgChatId);

        assertThat(chat).isPresent();
    }

    @Test
    @Transactional
    @Rollback
    public void multipleAddTest() {
        long tgChatId = 123;
        chatService.register(tgChatId);

        assertThatThrownBy(() -> chatService.register(tgChatId))
            .isInstanceOf(UserAlreadyRegisteredException.class);
    }

    @Test
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Rollback
    public void unregisterTest() {
        long tgChatId = 123;

        chatService.register(tgChatId);

        Optional<ChatEntity> before = chatRepository.getByTgChatId(tgChatId);
        chatService.unregister(tgChatId);
        Optional<ChatEntity> after = chatRepository.getByTgChatId(tgChatId);

        assertThat(before).isPresent();
        assertThat(after).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void multipleUnregisterTest() {
        long tgChatId = 123;

        chatService.register(tgChatId);
        chatService.unregister(tgChatId);

        assertThatThrownBy(() -> chatService.unregister(tgChatId))
            .isInstanceOf(NoSuchChatException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void unregisterWithLinksTest() {
        long tgChatId = 123;
        String uri = "test";

        chatService.register(123);
        linkService.add(tgChatId, URI.create(uri));

        Optional<ChatEntity> beforeChat = chatRepository.getByTgChatId(tgChatId);
        Optional<LinkEntity> beforeLink = linkRepository.getByUri(uri);
        chatService.unregister(tgChatId);
        Optional<ChatEntity> afterChat = chatRepository.getByTgChatId(tgChatId);
        Optional<LinkEntity> afterLink = linkRepository.getByUri(uri);

        assertThat(beforeChat).isPresent();
        assertThat(beforeLink).isPresent();
        assertThat(afterChat).isEmpty();
        assertThat(afterLink).isEmpty();
    }
}
