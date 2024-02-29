package edu.java.bot.model.command;

import com.pengrad.telegrambot.model.Update;

public interface Command {
    void execute(Update update);

    String getCommandText();

}
