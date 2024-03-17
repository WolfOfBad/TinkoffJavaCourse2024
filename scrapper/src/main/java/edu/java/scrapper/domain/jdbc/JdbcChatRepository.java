package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.dto.TelegramChat;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;

@AllArgsConstructor
@SuppressWarnings("MultipleStringLiterals")
public class JdbcChatRepository implements ChatRepository {
    private JdbcClient jdbcClient;

    @Override
    public List<TelegramChat> getAll() {
        String sql = "select * from chat";

        return jdbcClient.sql(sql)
            .query(TelegramChat.class)
            .list();
    }

    @Override
    public Optional<TelegramChat> getByTgChatId(long tgChatId) {
        String sql = "select * from chat where tg_chat_id = :tg_chat_id";

        return jdbcClient.sql(sql)
            .param("tg_chat_id", tgChatId)
            .query(TelegramChat.class)
            .optional();
    }

    @Override
    public TelegramChat add(long tgChatId) {
        String sql = "insert into chat (tg_chat_id) values (:tg_chat_id) returning *";

        return jdbcClient.sql(sql)
            .param("tg_chat_id", tgChatId)
            .query(TelegramChat.class)
            .single();
    }

    @Override
    public TelegramChat removeByTgChatId(long tgChatId) {
        String sql = "delete from chat where tg_chat_id = :tg_chat_id returning *";

        return jdbcClient.sql(sql)
            .param("tg_chat_id", tgChatId)
            .query(TelegramChat.class)
            .single();
    }
}
