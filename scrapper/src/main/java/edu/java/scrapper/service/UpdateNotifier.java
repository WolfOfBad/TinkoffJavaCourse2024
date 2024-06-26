package edu.java.scrapper.service;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.bot.dto.request.LinkUpdateRequest;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.dto.TelegramChat;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateNotifier {
    private BotClient botClient;

    public void notifyUsers(Link link, List<TelegramChat> chatList, String description) {
        botClient.send(LinkUpdateRequest.builder()
            .id(link.id())
            .uri(link.uri())
            .telegramChatId(chatList.stream().map(TelegramChat::tgChatId).toList())
            .description(description)
            .build()
        );
    }
}
