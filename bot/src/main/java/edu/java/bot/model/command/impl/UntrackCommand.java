package edu.java.bot.model.command.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.model.link.Link;
import edu.java.bot.model.link.parser.LinkParserManager;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.SendMessageService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UntrackCommand implements Command {
    private final UserRepository repository;
    private final LinkParserManager parser;
    private final SendMessageService sendMessageService;
    private final Logger logger = LogManager.getLogger();

    @Override
    public void execute(Update update) {
        User user = User.parse(update);

        try {
            String message = getMessage(update, user);

            sendMessageService.sendMessage(user, message);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    @Override
    public String getCommandText() {
        return "/untrack";
    }

    private String getMessage(Update update, User user) throws Exception {
        Optional<String> uri = parser.getUri(update);
        if (uri.isEmpty()) {
            return "Чтобы прекратить отслеживать ресурс, отправьте /untrack и ссылку на этот ресурс";

        }

        Optional<Link> link = parser.parse(uri.get());

        if (link.isEmpty()) {
            return "Данный ресурс, к сожалению, пока не поддерживается";

        }

        UserRepository.Result result = repository.deleteLink(user, link.get());

        return switch (result) {
            case OK -> "Вы прекратили отслеживание ссылки";

            case USER_NOT_EXIST -> "Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу";

            case LINK_NOT_EXIST -> "Вы не отслеживали эту ссылку. "
                + "Введите /list, чтобы увидеть все отслеживаемые ссылки";

            default ->
                throw new Exception("Unexpected switch result. Class: " + this.getClass() + " Result: " + result);
        };
    }

}
