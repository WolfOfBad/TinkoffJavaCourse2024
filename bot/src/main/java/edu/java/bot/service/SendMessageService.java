package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendMessageService {
    private final TelegramBot telegramBot;

    public void sendMessage(User user, String message) {
        SendMessage request = new SendMessage(user.id(), message)
            .parseMode(ParseMode.HTML);

        while (!sendRequest(request)) {
            sendRequest(request);
        }
    }

    private boolean sendRequest(SendMessage request) {
        SendResponse response = telegramBot.execute(request);

        return response.isOk();
    }

}
