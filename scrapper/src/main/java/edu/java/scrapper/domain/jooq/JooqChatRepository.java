package edu.java.scrapper.domain.jooq;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.dto.TelegramChat;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.scrapper.domain.jooq.generated.tables.Chat.CHAT;

@AllArgsConstructor
public class JooqChatRepository implements ChatRepository {
    private DSLContext dslContext;

    @Override
    public List<TelegramChat> getAll() {
        return dslContext.select(CHAT.fields())
            .from(CHAT)
            .fetchInto(TelegramChat.class);
    }

    @Override
    public Optional<TelegramChat> getByTgChatId(long tgChatId) {
        return dslContext.select(CHAT.fields())
            .from(CHAT)
            .where(CHAT.TG_CHAT_ID.eq(tgChatId))
            .fetchOptionalInto(TelegramChat.class);
    }

    @Override
    public TelegramChat add(long tgChatId) {
        return dslContext.insertInto(CHAT)
            .columns(CHAT.TG_CHAT_ID)
            .values(tgChatId)
            .returning(CHAT.fields())
            .fetchInto(TelegramChat.class)
            .getFirst();
    }

    @Override
    public TelegramChat removeByTgChatId(long tgChatId) {
        return dslContext.deleteFrom(CHAT)
            .where(CHAT.TG_CHAT_ID.eq(tgChatId))
            .returning(CHAT.fields())
            .fetchInto(TelegramChat.class)
            .getFirst();
    }
}


