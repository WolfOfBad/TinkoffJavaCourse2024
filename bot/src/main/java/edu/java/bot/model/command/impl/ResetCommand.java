package edu.java.bot.model.command.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.exception.scrapper.ScrapperException;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.service.SendMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ResetCommand implements Command {
    private final ScrapperClient client;
    private final SendMessageService sendMessageService;

    @Override
    public void execute(Update update) {
        User user = User.parse(update);

        try {
            client.deleteChat(user.id());
            sendMessageService.sendMessage(user, "Вы удалили все записи из бота. "
                + "Чтобы снова начать работу с ботом, введите /start");
        } catch (ScrapperException e) {
            sendMessageService.sendMessage(user, e.getTelegramMessage());
        }

    }

    @Override
    public String getCommandText() {
        return "/reset";
    }

}
