package edu.java.bot.model.command.impl;

import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.model.link.Link;
import edu.java.bot.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class TrackCommand implements Command {
    private final User user;
    private final UserRepository repository;
    private final Link link;

    @Override
    public void execute() {
        UserRepository.Result result = repository.addLink(user.id(), link);

        switch (result) {
            case OK -> user.sendMessage("Вы начали отслеживание сайта. "
                + "Теперь при обновлении контента по ссылке, вы получите уведомление");

            case USER_NOT_EXIST ->
                user.sendMessage("Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу с ботом");

            case LINK_ALREADY_EXIST -> user.sendMessage("Вы уже отслеживаете эту ссылку. "
                + "Введите /list, чтобы увидеть все отслеживаемые ссылки");

            default -> log.error("Unexpected switch result. Class: " + this.getClass() + " Result: " + result);
        }

    }
}
