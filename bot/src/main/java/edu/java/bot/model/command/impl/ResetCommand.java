package edu.java.bot.model.command.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ResetCommand implements Command {
    private final ObjectProvider<User> userObjectProvider;
    private final UserRepository repository;
    private final Logger logger = LogManager.getLogger();

    @Override
    public void execute(Update update) {
        User user = userObjectProvider.getObject(update);

        UserRepository.Result result = repository.deleteUser(user.getId());

        switch (result) {
            case OK -> user.sendMessage("Вы удалили все записи из бота. "
                + "Чтобы снова начать работу с ботом, введите /start");

            case USER_NOT_EXIST -> user.sendMessage("Вы и так не были зарегестрированы в боте.");

            default -> logger.error("Unexpected switch result. Class: " + this.getClass() + " Result: " + result);
        }

    }

    @Override
    public String getCommandText() {
        return "/reset";
    }

}
