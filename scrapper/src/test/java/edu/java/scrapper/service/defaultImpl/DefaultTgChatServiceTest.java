package edu.java.scrapper.service.defaultImpl;

import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.dto.TelegramChat;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.exception.UserAlreadyRegisteredException;
import edu.java.scrapper.integration.IntegrationTest;
import edu.java.scrapper.service.TgChatService;
import java.net.URI;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestPropertySource(properties = {"app.database-access-type=jdbc"})
public class DefaultTgChatServiceTest extends IntegrationTest {
    @Autowired
    private TgChatService tgChatService;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private JdbcClient client;

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        long tgChatId = 123;

        tgChatService.register(tgChatId);

        TelegramChat chat = client.sql("select * from chat")
            .query(TelegramChat.class)
            .single();

        assertThat(chat.tgChatId()).isEqualTo(tgChatId);
    }

    @Test
    @Transactional
    @Rollback
    public void multipleAddTest() {
        long tgChatId = 123;

        tgChatService.register(tgChatId);

        assertThatThrownBy(() -> tgChatService.register(tgChatId))
            .isInstanceOf(UserAlreadyRegisteredException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void unregisterTest() {
        long tgChatId = 123;

        tgChatService.register(tgChatId);
        linkRepository.addAndSubscribe(URI.create("test"), tgChatId);

        Optional<TelegramChat> before = client.sql("select * from chat")
            .query(TelegramChat.class)
            .optional();
        tgChatService.unregister(tgChatId);
        Optional<TelegramChat> after = client.sql("select * from chat")
            .query(TelegramChat.class)
            .optional();

        assertThat(before).isPresent();
        assertThat(after).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void multipleUnregisterTest() {
        long tgChatId = 123;

        assertThatThrownBy(() -> tgChatService.unregister(tgChatId))
            .isInstanceOf(NoSuchChatException.class);
    }

}
