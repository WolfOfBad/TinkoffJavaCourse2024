package edu.java.bot.service;

import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.model.command.impl.FailCommand;
import edu.java.bot.model.command.impl.HelpCommand;
import edu.java.bot.model.command.impl.ListCommand;
import edu.java.bot.model.command.impl.ResetCommand;
import edu.java.bot.model.command.impl.StartCommand;
import edu.java.bot.model.command.impl.TrackCommand;
import edu.java.bot.model.command.impl.UntrackCommand;
import edu.java.bot.model.link.Link;
import edu.java.bot.model.link.parser.LinkParser;
import java.util.Arrays;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommandParser {
    private final ApplicationContext context;
    private final LinkParser parserChain;

    public Command parse(Update update) {
        Optional<MessageEntity> entity = getCommandEntity(update);

        if (entity.isEmpty()) {
            return context.getBean(
                FailCommand.class,
                getUser(update),
                "Введите команду бота. Вы можете посмотреть список команд, отправив /help"
            );
        }

        int length = entity.get().length();
        String command = update.message().text().substring(0, length);

        User user = getUser(update);
        return switch (command) {
            case "/start" -> context.getBean(StartCommand.class, user);
            case "/reset" -> context.getBean(ResetCommand.class, user);

            case "/track" -> linkParse(update, command, user, TrackCommand.class);
            case "/untrack" -> linkParse(update, command, user, UntrackCommand.class);

            case "/list" -> context.getBean(ListCommand.class, user);

            case "/help" -> context.getBean(HelpCommand.class, user);

            default -> context.getBean(
                FailCommand.class,
                user,
                "Неизвестная команда. Чтобы посмотреть список команд, используйте /help"
            );
        };
    }

    private Optional<MessageEntity> getCommandEntity(Update update) {
        if (update.message().entities() == null) {
            return Optional.empty();
        }
        return Arrays.stream(update.message().entities())
            .filter(e -> e.type() == MessageEntity.Type.bot_command)
            .findAny();
    }

    private User getUser(Update update) {
        long id = update.message().from().id();

        return context.getBean(User.class, id);
    }

    private Command linkParse(Update update, String command, User user, Class<?> cls) {
        if (update.message().text().length() == command.length()) {
            return context.getBean(
                FailCommand.class,
                user,
                "Вместе с командой " + command + " отправьте ссылку на ресурс"
            );
        }

        Optional<Link> link = parserChain.parse(update.message().text().substring(command.length() + 1));
        return link.map(value -> (Command) context.getBean(cls, user, value))
            .orElseGet(() -> context.getBean(
                FailCommand.class,
                user,
                "Данный ресурс пока не поддерживается"
            ));

    }

}
