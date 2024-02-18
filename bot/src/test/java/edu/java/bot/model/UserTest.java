package edu.java.bot.model;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserTest {

    @Test
    void sendMessageTest() {
        SendResponse response = mock(SendResponse.class);
        when(response.isOk()).thenReturn(true);

        TelegramBot bot = mock(TelegramBot.class);
        when(bot.execute(any(SendMessage.class))).thenReturn(response);

        User user = new User(1, bot);
        user.sendMessage("message");

        verify(bot, times(1)).execute(any(SendMessage.class));
    }
}
