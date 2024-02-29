package edu.java.bot.model;

import com.pengrad.telegrambot.model.Update;

public record User(
    long id
) {
    public static User parse(Update update) {
        return new User(update.message().from().id());
    }
}
