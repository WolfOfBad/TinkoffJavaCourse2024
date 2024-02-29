package edu.java.bot.model.command.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class StartCommand implements Command {
    private final ObjectProvider<User> userObjectProvider;
    private final UserRepository repository;

    @Override
    public void execute(Update update) {
        User user = userObjectProvider.getObject(update);

        UserRepository.Result result = repository.register(user.getId());

        switch (result) {
            case OK -> user.sendMessage("Вы успешно запустили бота. Теперь вы можете отслеживать ссылки."
                + " Чтобы узнать больше, используйте команду /help");

            case USER_ALREADY_EXIST -> user.sendMessage("Вы уже зарегестрированы в боте. "
                + "Чтобы сбросить ссылки отправьте команду /reset");

            default -> log.error("Unexpected switch result. Class: " + this.getClass() + " Result: " + result);
        }

    }

    @Override
    public String getCommandText() {
        return "/start";
    }

}
