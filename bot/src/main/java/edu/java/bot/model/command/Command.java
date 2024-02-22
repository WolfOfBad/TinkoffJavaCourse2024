package edu.java.bot.model.command;

import com.pengrad.telegrambot.model.Update;

@FunctionalInterface
public interface Command {
    void execute(Update update);

}
