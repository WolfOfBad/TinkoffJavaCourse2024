package edu.java.bot.model;

import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import java.net.URI;
import java.util.Arrays;

public record Link(
    URI uri
) {
    public static Link parse(Update update) {
        int length = Arrays.stream(update.message().entities())
            .filter(e -> e.type() == MessageEntity.Type.bot_command)
            .findAny()
            .get()
            .length();

        return new Link(URI.create(update.message().text().substring(length + 1)));
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Link)) {
            return false;
        }
        return uri.equals(((Link) object).uri);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }

}
