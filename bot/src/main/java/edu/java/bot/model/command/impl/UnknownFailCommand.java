package edu.java.bot.model.command.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.service.SendMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UnknownFailCommand implements Command {
    private final SendMessageService sendMessageService;

    @Override
    public void execute(Update update) {
        User user = User.parse(update);

        sendMessageService.sendMessage(user, "Неизвестная команда. Чтобы посмотреть список команд, используйте /help");
    }

    @Override
    public String getCommandText() {
        return null;
    }

}
