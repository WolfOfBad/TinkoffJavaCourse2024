package edu.java.bot.service;

import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.command.Command;
import edu.java.bot.model.command.impl.SimpleTextFailCommand;
import edu.java.bot.model.command.impl.UnknownFailCommand;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CommandParser {
    private final Map<String, Command> commands;
    private final SimpleTextFailCommand simpleTextFailCommand;
    private final UnknownFailCommand unknownFailCommand;

    public CommandParser(
        @Qualifier("commandMap") Map<String, Command> commands,
        SimpleTextFailCommand simpleTextFailCommand,
        UnknownFailCommand unknownFailCommand
    ) {
        this.commands = commands;
        this.simpleTextFailCommand = simpleTextFailCommand;
        this.unknownFailCommand = unknownFailCommand;
    }

    public Command parse(Update update) {
        Optional<MessageEntity> entity;

        if (update.message().entities() == null) {
            entity = Optional.empty();
        } else {
            entity = Arrays.stream(update.message().entities())
                .filter(e -> e.type() == MessageEntity.Type.bot_command)
                .findFirst();
        }

        if (entity.isEmpty()) {
            return simpleTextFailCommand;
        }

        int length = entity.get().length();
        String command = update.message().text().substring(0, length);

        Command result = commands.get(command);

        if (result == null) {
            return unknownFailCommand;
        }

        return result;
    }

}
