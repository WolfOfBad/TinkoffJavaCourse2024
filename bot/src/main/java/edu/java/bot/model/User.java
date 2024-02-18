package edu.java.bot.model;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public record User(
    long id,
    TelegramBot bot
) {
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
