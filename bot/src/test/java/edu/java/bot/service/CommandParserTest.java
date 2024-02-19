package edu.java.bot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.model.command.impl.FailCommand;
import edu.java.bot.model.command.impl.HelpCommand;
import edu.java.bot.model.command.impl.ListCommand;
import edu.java.bot.model.command.impl.ResetCommand;
import edu.java.bot.model.command.impl.StartCommand;
import edu.java.bot.model.command.impl.TrackCommand;
import edu.java.bot.model.command.impl.UntrackCommand;
import edu.java.bot.model.link.Link;
import edu.java.bot.model.link.parser.LinkParser;
import java.net.URI;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CommandParserTest {

    @Mock
    private ApplicationContext context;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private com.pengrad.telegrambot.model.User user;

    @Mock
    private MessageEntity messageEntity;

    @Mock
    private LinkParser linkParser;

    @BeforeEach
    public void before() {
        when(update.message()).thenReturn(message);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);

        when(context.getBean(eq(User.class), anyLong())).thenReturn(new User(1L, null));

        lenient().when(context.getBean(
            eq(Link.class),
            any(Update.class)
        )).thenReturn(mock(Link.class));

        // command mocks
        lenient().when(context.getBean(
            eq(FailCommand.class),
            any(User.class),
            any(String.class)
        )).thenReturn(mock(FailCommand.class));
        lenient().when(context.getBean(
            eq(StartCommand.class),
            any(User.class)
        )).thenReturn(mock(StartCommand.class));
        lenient().when(context.getBean(
            eq(HelpCommand.class),
            any(User.class)
        )).thenReturn(mock(HelpCommand.class));
        lenient().when(context.getBean(
            eq(ListCommand.class),
            any(User.class)
        )).thenReturn(mock(ListCommand.class));
        lenient().when(context.getBean(
            eq(ResetCommand.class),
            any(User.class)
        )).thenReturn(mock(ResetCommand.class));
        lenient().when(context.getBean(
            eq(TrackCommand.class),
            any(User.class),
            any(Link.class)
        )).thenReturn(mock(TrackCommand.class));
        lenient().when(context.getBean(
            eq(UntrackCommand.class),
            any(User.class),
            any(Link.class)
        )).thenReturn(mock(UntrackCommand.class));

        lenient().when(linkParser.parse(any(String.class)))
            .thenReturn(Optional.of(new Link(URI.create("uri"))));
    }

    @ParameterizedTest
    @MethodSource("args")
    public void correctParseResultTest(String command, Class<?> cls) {
        if (command.compareTo("not command") != 0) {
            configureCommand(command);
        }

        CommandParser parser = new CommandParser(context, linkParser);

        assertThat(parser.parse(update)).isInstanceOf(cls);
    }

    private void configureCommand(String command) {
        when(message.text()).thenReturn(command);

        when(messageEntity.type()).thenReturn(MessageEntity.Type.bot_command);
        when(messageEntity.length()).thenReturn(command.split(" ")[0].length());

        when(update.message().entities()).thenReturn(new MessageEntity[] {messageEntity});
    }

    private static Arguments[] args() {
        return new Arguments[] {
            Arguments.of("not command", FailCommand.class),
            Arguments.of("/unknown", FailCommand.class),
            Arguments.of("/start", StartCommand.class),
            Arguments.of("/help", HelpCommand.class),
            Arguments.of("/list", ListCommand.class),
            Arguments.of("/track uri", TrackCommand.class),
            Arguments.of("/track", FailCommand.class),
            Arguments.of("/untrack uri", UntrackCommand.class),
            Arguments.of("/untrack", FailCommand.class),
            Arguments.of("/reset", ResetCommand.class)
        };
    }

}
