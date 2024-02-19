package edu.java.bot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.configuration.ApplicationConfigProperties;
import edu.java.bot.model.command.Command;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BotServiceTest {
    @Mock
    private ApplicationConfigProperties properties;

    @Mock
    private CommandParser parser;

    @Mock
    private Update update;

    @Test
    public void processTest() {
        when(properties.telegramToken()).thenReturn("test_token");
        when(parser.parse(any(Update.class))).thenReturn(mock(Command.class));
        when(update.message()).thenReturn(mock(Message.class));

        BotService service = new BotService(properties, parser);
        service.process(List.of(update));
        service.close();

        verify(parser, times(1)).parse(any(Update.class));
    }

}
