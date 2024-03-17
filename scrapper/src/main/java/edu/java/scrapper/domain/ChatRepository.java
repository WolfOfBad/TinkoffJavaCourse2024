package edu.java.scrapper.domain;

import edu.java.scrapper.domain.dto.TelegramChat;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    List<TelegramChat> getAll();

    Optional<TelegramChat> getByTgChatId(long tgChatId);

    TelegramChat add(long tgChatId);

    TelegramChat removeByTgChatId(long tgChatId);
}
