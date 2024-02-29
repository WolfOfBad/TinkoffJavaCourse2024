package edu.java.bot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.command.Command;
import edu.java.bot.model.command.impl.HelpCommand;
import edu.java.bot.model.command.impl.ListCommand;
import edu.java.bot.model.command.impl.ResetCommand;
import edu.java.bot.model.command.impl.SimpleTextFailCommand;
import edu.java.bot.model.command.impl.StartCommand;
import edu.java.bot.model.command.impl.TrackCommand;
import edu.java.bot.model.command.impl.UnknownFailCommand;
import edu.java.bot.model.command.impl.UntrackCommand;
import edu.java.bot.model.link.Link;
import edu.java.bot.model.link.parser.LinkParserManager;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandParserTest {

    @Mock
    private ApplicationContext context;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private MessageEntity messageEntity;

    @Mock
    private LinkParserManager linkParser;

    private final Map<String, Command> commandMap = Map.ofEntries(
        Map.entry("/start", mock(StartCommand.class)),
        Map.entry("/reset", mock(ResetCommand.class)),
        Map.entry("/track", mock(TrackCommand.class)),
        Map.entry("/untrack", mock(UntrackCommand.class)),
        Map.entry("/help", mock(HelpCommand.class)),
        Map.entry("/list", mock(ListCommand.class))
    );

    @BeforeEach
    public void before() {
        lenient().when(context.getBean(
            eq(Link.class),
            any(Update.class)
        )).thenReturn(mock(Link.class));

        lenient().when(linkParser.parse(any(String.class)))
            .thenReturn(Optional.of(new Link(URI.create("uri"))));
    }

    @ParameterizedTest
    @MethodSource("args")
    public void correctParseResultTest(String command, Class<?> cls) {
        when(update.message()).thenReturn(message);
        if (command.compareTo("not command") != 0) {
            configureCommand(command);
        }

        CommandParser parser = new CommandParser(
            commandMap,
            mock(SimpleTextFailCommand.class),
            mock(UnknownFailCommand.class)
        );

        Command result = parser.parse(update);

        assertThat(result).isInstanceOf(cls);
    }

    private void configureCommand(String command) {

        when(messageEntity.type()).thenReturn(MessageEntity.Type.bot_command);
        when(messageEntity.length()).thenReturn(command.split(" ")[0].length());

        when(message.text()).thenReturn(command);
        when(message.entities()).thenReturn(new MessageEntity[] {messageEntity});
    }

    private static Arguments[] args() {
        return new Arguments[] {
            Arguments.of("not command", SimpleTextFailCommand.class),
            Arguments.of("/unknown", UnknownFailCommand.class),
            Arguments.of("/start", StartCommand.class),
            Arguments.of("/help", HelpCommand.class),
            Arguments.of("/list", ListCommand.class),
            Arguments.of("/track uri", TrackCommand.class),
            Arguments.of("/track", TrackCommand.class),
            Arguments.of("/untrack uri", UntrackCommand.class),
            Arguments.of("/untrack", UntrackCommand.class),
            Arguments.of("/reset", ResetCommand.class)
        };
    }

}

