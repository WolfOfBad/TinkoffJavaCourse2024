package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BotServiceTest {
    @Mock
    private CommandParser parser;

    @Mock
    private Update update;

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
    public void processTest() {
        when(parser.parse(any(Update.class))).thenReturn(mock(Command.class));
        when(update.message()).thenReturn(mock(Message.class));

        BotService service = new BotService(bot, parser);
        service.process(List.of(update));
        service.close();

        verify(parser, times(1)).parse(any(Update.class));
    }

    @Test
    public void sendMessageTest() {
        BotService service = new BotService(bot, parser);
        service.sendMessage(new User(1), "message");
        service.close();

        verify(bot, times(2)).execute(any());
    }

}
