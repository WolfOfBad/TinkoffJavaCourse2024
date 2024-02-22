package edu.java.bot.model;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTest {
    @Mock
    private SendResponse response;

    @Mock
    private TelegramBot bot;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private com.pengrad.telegrambot.model.User user;

    @BeforeEach
    public void before() {
        when(response.isOk()).thenReturn(true);
        when(bot.execute(any(SendMessage.class))).thenReturn(response);
        when(update.message()).thenReturn(message);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
    }

    @Test
    void sendMessageTest() {
        String message = "message";

        User user = new User(update);
        user.setBot(bot);
        user.sendMessage(message);

        verify(bot, times(1)).execute(any(SendMessage.class));
    }
}
