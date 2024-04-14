package edu.java.bot.model.command.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import edu.java.bot.exception.scrapper.ScrapperException;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.service.SendMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ListCommand implements Command {
    private final ScrapperClient client;
    private final SendMessageService sendMessageService;

    @Override
    public void execute(Update update) {
        User user = User.parse(update);

        try {
            ListLinksResponse links = client.getTrackedLinks(user.id());

            String message = links.links().stream()
                .map(e -> e.uri().toString())
                .reduce("", (s, e) -> s + "\n" + e);

            sendMessageService.sendMessage(user, "Список отслеживаемых ссылок:\n" + message);
        } catch (ScrapperException e) {
            sendMessageService.sendMessage(user, e.getTelegramMessage());
        }
    }

    @Override
    public String getCommandText() {
        return "/list";
    }

}
