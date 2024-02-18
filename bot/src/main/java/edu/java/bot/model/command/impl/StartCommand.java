package edu.java.bot.model.command.impl;

import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class StartCommand implements Command {
    private final User user;
    private final UserRepository repository;

    @Override
    public void execute() {
        UserRepository.Result result = repository.register(user.id());

        switch (result) {
            case OK -> user.sendMessage("Вы успешно запустили бота. Теперь вы можете отслеживать ссылки."
                + " Чтобы узнать больше, используйте команду /help");

            case USER_ALREADY_EXIST -> user.sendMessage("Вы уже зарегестрированы в боте. "
                + "Чтобы сбросить ссылки отправьте команду /reset");

            default -> log.error("Unexpected switch result. Class: " + this.getClass() + " Result: " + result);
        }

    }
}
