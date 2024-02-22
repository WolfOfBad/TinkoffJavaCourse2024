package edu.java.bot.model;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public final class User {
    @Getter
    private final long id;
    private TelegramBot bot;

    public User(Update update) {
        this.id = update.message().from().id();
    }

    @Autowired
    public void setBot(TelegramBot bot) {
        this.bot = bot;
    }

    public void sendMessage(String message) {
        SendMessage request = new SendMessage(id, message)
            .parseMode(ParseMode.HTML);

        while (!sendRequest(request)) {
            sendRequest(request);
        }

    }

    private boolean sendRequest(SendMessage request) {
        SendResponse response = bot.execute(request);

        return response.isOk();
    }

}
