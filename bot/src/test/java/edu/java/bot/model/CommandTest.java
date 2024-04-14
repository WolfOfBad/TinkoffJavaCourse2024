package edu.java.bot.model;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
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
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.SendMessageService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommandTest {
    @Mock
    private ScrapperClient client;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private com.pengrad.telegrambot.model.User user;

    @Mock
    private LinkParserManager parser;

    @Mock
    private SendMessageService botService;

    @BeforeEach
    public void before() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.from()).thenReturn(user);
        lenient().when(user.id()).thenReturn(1L);
    }

    @Test
    public void helpCommandTest() {
        Command command = new HelpCommand(botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
    }

    @Test
    public void listCommandTest() {
        when(client.getTrackedLinks(any())).thenReturn(new ListLinksResponse(
            List.of(new LinkResponse(1, URI.create("link"))),
            1
        ));

        Command command = new ListCommand(client, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues().getFirst()).isEqualTo("Список отслеживаемых ссылок:\nlink\n");
    }

    @Test
    public void userNotExistListCommandTest() {
        when(client.getTrackedLinks(any())).thenReturn(new ListLinksResponse(
                List.of(),
                0
            )
        );

        Command command = new ListCommand(client, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу с ботом");
    }

    @Test
    public void noLinksListCommandTest() {
        when(client.getTrackedLinks(any())).thenReturn(null);

        Command command = new ListCommand(client, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo("Сейчас вы не отслеживаете никаких ссылок");
    }

    @Test
    public void resetCommandTest() {
        Command command = new ResetCommand(client, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы удалили все записи из бота. Чтобы снова начать работу с ботом, введите /start");
    }

    @Test
    public void userNotExistResetCommandTest() {
        Command command = new ResetCommand(client, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы и так не были зарегестрированы в боте.");
    }

    @Test
    public void startCommandTest() {
        Command command = new StartCommand(client, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы успешно запустили бота. Теперь вы можете отслеживать ссылки. "
                + "Чтобы узнать больше, используйте команду /help"
        );
    }

    @Test
    public void userAlreadyExistStartCommandTest() {
        Command command = new StartCommand(client, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы уже зарегестрированы в боте. Чтобы сбросить ссылки отправьте команду /reset");
    }

    @Test
    public void trackCommandTest() {
        when(client.addLink(any(), any())).thenReturn(null);
        when(parser.parse(any(String.class))).thenReturn(Optional.of(mock(Link.class)));
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));

        Command command = new TrackCommand(client, parser, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы начали отслеживание сайта. Теперь при обновлении контента по ссылке, вы получите уведомление");
    }

    @Test
    public void userNotExistTrackCommandTest() {
        when(client.addLink(any(), any())).thenReturn(mock(LinkResponse.class));
        when(parser.parse(any(String.class))).thenReturn(Optional.of(mock(Link.class)));
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));

        Command command = new TrackCommand(client, parser, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу с ботом");
    }

    @Test
    public void linkAlreadyExistTrackCommandTest() {
        when(client.addLink(any(), any())).thenReturn(null);
        when(parser.parse(any(String.class))).thenReturn(Optional.of(mock(Link.class)));
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));

        Command command = new TrackCommand(client, parser, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы уже отслеживаете эту ссылку. Введите /list, чтобы увидеть все отслеживаемые ссылки");
    }

    @Test
    public void noLinkTrackCommandTest() {
        when(parser.getUri(any(Update.class))).thenReturn(Optional.empty());

        Command command = new TrackCommand(client, parser, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Чтобы начать отслеживать ресурс, отправьте /track и ссылку на этот ресурс");
    }

    @Test
    public void notSupportedLinkTrackCommandTest() {
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));
        when(parser.parse(any(String.class))).thenReturn(Optional.empty());

        Command command = new TrackCommand(client, parser, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Данный ресурс, к сожалению, пока не поддерживается");
    }

    @Test
    public void untrackCommandTest() {
        when(client.deleteLink(any(), any())).thenReturn(null);
        when(parser.parse(any(String.class))).thenReturn(Optional.of(mock(Link.class)));
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));

        Command command = new UntrackCommand(client, parser, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo("Вы прекратили отслеживание ссылки");
    }

    @Test
    public void userNotExistUntrackCommandTest() {
        when(client.deleteLink(any(), any())).thenReturn(null);
        when(parser.parse(any(String.class))).thenReturn(Optional.of(mock(Link.class)));
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));

        Command command = new UntrackCommand(client, parser, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу");
    }

    @Test
    public void linkNotExistUntrackCommandTest() {
        when(client.deleteLink(any(), any())).thenReturn(null);
        when(parser.parse(any(String.class))).thenReturn(Optional.of(mock(Link.class)));
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));

        Command command = new UntrackCommand(client, parser, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы не отслеживали эту ссылку. Введите /list, чтобы увидеть все отслеживаемые ссылки");
    }

    @Test
    public void noLinkUntrackCommandTest() {
        when(parser.getUri(any(Update.class))).thenReturn(Optional.empty());

        Command command = new UntrackCommand(client, parser, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Чтобы прекратить отслеживать ресурс, отправьте /untrack и ссылку на этот ресурс");
    }

    @Test
    public void notSupportedLinkUntrackCommandTest() {
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));
        when(parser.parse(any(String.class))).thenReturn(Optional.empty());

        Command command = new UntrackCommand(client, parser, botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Данный ресурс, к сожалению, пока не поддерживается");
    }

    @Test
    public void simpleTextFailCommandTest() {
        Command command = new SimpleTextFailCommand(botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
    }

    @Test
    public void unknownFailCommand() {
        Command command = new UnknownFailCommand(botService);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(botService).sendMessage(any(User.class), captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
    }

    @ParameterizedTest
    @MethodSource("args")
    public void getCommandTextTest(Command command, String expected) {
        String result = command.getCommandText();

        assertThat(result).isEqualTo(expected);
    }

    private static Arguments[] args() {
        return new Arguments[] {
            Arguments.of(new HelpCommand(null), "/help"),
            Arguments.of(new ListCommand(null, null), "/list"),
            Arguments.of(new ResetCommand(null, null), "/reset"),
            Arguments.of(new StartCommand(null, null), "/start"),
            Arguments.of(new TrackCommand(null, null, null), "/track"),
            Arguments.of(new UntrackCommand(null, null, null), "/untrack"),

            Arguments.of(new SimpleTextFailCommand(null), null),
            Arguments.of(new UnknownFailCommand(null), null)
        };
    }

}

