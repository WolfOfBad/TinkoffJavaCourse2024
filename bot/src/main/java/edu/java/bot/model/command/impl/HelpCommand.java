package edu.java.bot.model.command.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.service.SendMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HelpCommand implements Command {
    private final SendMessageService sendMessageService;

    @Override
    public void execute(Update update) {
        User user = User.parse(update);

        sendMessageService.sendMessage(
            user,
            """
                Список команд бота:
                /start начать работу с ботом
                /reset сбросить все отслеживаемые ссылки
                /track начать отслеживание сслыки
                /untrack прекратить отслеживанте ссылки
                /list посмотреть список отслеживаемых сайтов
                /help список доступных команд
                """
        );
    }

    @Override
    public String getCommandText() {
        return "/help";
    }

}
