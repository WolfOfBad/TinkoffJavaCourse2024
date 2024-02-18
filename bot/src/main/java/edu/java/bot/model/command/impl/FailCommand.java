package edu.java.bot.model.command.impl;

import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FailCommand implements Command {
    private final User user;
    private final String message;

    @Override
    public void execute() {
        user.sendMessage(message);
    }
}
