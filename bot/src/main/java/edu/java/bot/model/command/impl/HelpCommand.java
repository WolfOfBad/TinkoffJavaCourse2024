package edu.java.bot.model.command.impl;

import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HelpCommand implements Command {
    private final User user;

    @Override
    public void execute() {
        user.sendMessage("""
            Список команд бота:
            /start начать работу с ботом
            /reset сбросить все отслеживаемые ссылки
            /track начать отслеживание сслыки
            /untrack прекратить отслеживанте ссылки
            /list посмотреть список отслеживаемых сайтов
            /help список доступных команд
            """);
    }
}
