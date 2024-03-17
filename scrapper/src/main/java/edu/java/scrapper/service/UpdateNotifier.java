package edu.java.scrapper.service;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.dto.TelegramChat;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateNotifier {
    private BotClient botClient;

    public void notifyUsers(Link link, List<TelegramChat> chatList) {
        botClient.sendUpdate(
            link.id(),
            link.uri().toString(),
            "todo",
            chatList.stream().map(TelegramChat::id).toList()
        );
    }
}
