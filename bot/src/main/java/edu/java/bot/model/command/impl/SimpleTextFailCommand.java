package edu.java.bot.model.command.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.service.BotService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SimpleTextFailCommand implements Command {
    private final BotService botService;

    @Override
    public void execute(Update update) {
        User user = User.parse(update);

        botService.sendMessage(user, "Введите команду бота. Вы можете посмотреть список команд, отправив /help");
    }

    @Override
    public String getCommandText() {
        return null;
    }

}
