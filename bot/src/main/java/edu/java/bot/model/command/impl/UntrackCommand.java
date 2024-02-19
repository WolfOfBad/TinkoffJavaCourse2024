package edu.java.bot.model.command.impl;

import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.model.link.Link;
import edu.java.bot.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class UntrackCommand implements Command {
    private final User user;
    private final UserRepository repository;
    private final Link link;

    @Override
    public void execute() {
        UserRepository.Result result = repository.deleteLink(user.id(), link);

        switch (result) {
            case OK -> user.sendMessage("Вы прекратили отслеживание ссылки");

            case USER_NOT_EXIST ->
                user.sendMessage("Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу");

            case LINK_NOT_EXIST -> user.sendMessage("Вы не отслеживали эту ссылку. "
                + "Введите /list, чтобы увидеть все отслеживаемые ссылки");

            default -> log.error("Unexpected switch result. Class: " + this.getClass() + " Result: " + result);
        }
    }
}
