package edu.java.bot.model.command.impl;

import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class ResetCommand implements Command {
    private final User user;
    private final UserRepository repository;

    @Override
    public void execute() {
        UserRepository.Result result = repository.deleteUser(user.id());

        switch (result) {
            case OK -> user.sendMessage("Вы удалили все записи из бота. "
                + "Чтобы снова начать работу с ботом, введите /start");

            case USER_NOT_EXIST -> user.sendMessage("Вы и так не были зарегестрированы в боте.");

            default -> log.error("Unexpected switch result. Class: " + this.getClass() + " Result: " + result);
        }

    }
}
