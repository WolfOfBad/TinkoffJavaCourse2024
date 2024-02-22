package edu.java.bot.model.command.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UnknownFailCommand implements Command {
    private final ObjectProvider<User> userObjectProvider;

    @Override
    public void execute(Update update) {
        User user = userObjectProvider.getObject(update);

        user.sendMessage("Неизвестная команда. Чтобы посмотреть список команд, используйте /help");
    }

}
