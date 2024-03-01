package edu.java.bot.service;

import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import edu.java.bot.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatesSendService {
    private final SendMessageService sendMessageService;

    public void send(LinkUpdateRequest request) {
        String message = "Новое обновление по ссылке " + request.uri() + ":\n" + request.description();
        for (long id : request.telegramChatId()) {
            User user = new User(id);

            sendMessageService.sendMessage(user, message);
        }
    }

}
