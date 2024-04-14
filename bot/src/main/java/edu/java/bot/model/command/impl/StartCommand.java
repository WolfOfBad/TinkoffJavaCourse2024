package edu.java.bot.model.command.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.exception.scrapper.ScrapperException;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.service.SendMessageService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StartCommand implements Command {
    private final ScrapperClient client;
    private final SendMessageService sendMessageService;
    private final Logger logger = LogManager.getLogger();

    @Override
    public void execute(Update update) {
        User user = User.parse(update);

        try {
            client.registerChat(user.id());

            sendMessageService.sendMessage(user, "Вы успешно запустили бота. "
                + "Теперь вы можете отслеживать ссылки. Чтобы узнать больше, используйте команду /help");
        } catch (ScrapperException e) {
            sendMessageService.sendMessage(user, e.getTelegramMessage());
        }

    }

    @Override
    public String getCommandText() {
        return "/start";
    }

}
