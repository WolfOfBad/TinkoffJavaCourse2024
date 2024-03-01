package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.model.User;
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
public class SendMessageServiceTest {
    @Mock
    private TelegramBot bot;

    @Mock
    private SendResponse response;

    @BeforeEach
    public void before() {
        when(response.isOk()).thenReturn(true);
        when(bot.execute(any())).thenReturn(response);
    }

    @Test
    public void sendMessageTest() {
        SendMessageService service = new SendMessageService(bot);
        service.sendMessage(new User(1), "message");

        verify(bot, times(1)).execute(any());
    }

}
