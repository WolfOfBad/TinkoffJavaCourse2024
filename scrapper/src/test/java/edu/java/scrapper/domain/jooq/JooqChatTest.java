package edu.java.scrapper.domain.jooq;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.dto.TelegramChat;
import edu.java.scrapper.integration.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    properties = {"app.database-access-type=jooq", "app.scheduler.enable=false"}
)
public class JooqChatTest extends IntegrationTest {
    @Autowired
    private ChatRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        long tgChatId = 123;

        TelegramChat chat = repository.add(tgChatId);

        TelegramChat result = jdbcClient.sql("select * from chat where tg_chat_id = :tg_chat_id")
            .param("tg_chat_id", tgChatId)
            .query(TelegramChat.class)
            .single();

        assertThat(chat).isEqualTo(result);
    }

    @Test
    @Transactional
    @Rollback
    public void removeByTgChatIdTest() {
        long tgChatId = 123;
        TelegramChat chat = repository.add(tgChatId);

        repository.removeByTgChatId(chat.tgChatId());

        Optional<TelegramChat> result = jdbcClient.sql("select * from chat where tg_chat_id = :tg_chat_id")
            .param("tg_chat_id", chat.tgChatId())
            .query(TelegramChat.class)
            .optional();

        assertThat(result).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void getByTgChatIdTest() {
        long tgChatId = 123;
        TelegramChat chat = repository.add(tgChatId);

        Optional<TelegramChat> result = repository.getByTgChatId(tgChatId);

        assertThat(result.get()).isEqualTo(chat);
    }

    @Test
    @Transactional
    @Rollback
    public void getAllTest() {
        TelegramChat chat1 = repository.add(1);
        TelegramChat chat2 = repository.add(2);
        TelegramChat chat3 = repository.add(3);

        List<TelegramChat> result = repository.getAll();

        assertThat(result).asList().isEqualTo(List.of(chat1, chat2, chat3));
    }

}
