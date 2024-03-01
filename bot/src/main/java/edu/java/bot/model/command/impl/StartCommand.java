package edu.java.bot.model.command.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.SendMessageService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StartCommand implements Command {
    private final UserRepository repository;
    private final SendMessageService sendMessageService;
    private final Logger logger = LogManager.getLogger();

    @Override
    public void execute(Update update) {
        User user = User.parse(update);

        UserRepository.Result result = repository.register(user);

        switch (result) {
            case OK -> sendMessageService.sendMessage(user, "Вы успешно запустили бота. "
                + "Теперь вы можете отслеживать ссылки. Чтобы узнать больше, используйте команду /help");

            case USER_ALREADY_EXIST -> sendMessageService.sendMessage(user, "Вы уже зарегестрированы в боте. "
                + "Чтобы сбросить ссылки отправьте команду /reset");

            default -> logger.error("Unexpected switch result. Class: " + this.getClass() + " Result: " + result);
        }

    }

    @Override
    public String getCommandText() {
        return "/start";
    }

}
