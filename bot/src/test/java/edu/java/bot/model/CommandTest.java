package edu.java.bot.model;

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
import edu.java.bot.model.link.parser.LinkParser;
import edu.java.bot.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommandTest {

    @Mock
    private User user;

    @Mock
    private UserRepository repository;

    @Mock
    private Update update;

    @Mock
    private LinkParser parser;

    @Mock
    private ObjectProvider<User> userObjectProvider;

    @BeforeEach
    public void before() {
        when(userObjectProvider.getObject(any(Update.class))).thenReturn(user);
    }

    @Test
    public void helpCommandTest() {
        Command command = new HelpCommand(userObjectProvider);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
    }

    @Test
    public void listCommandTest() {
        when(repository.getLinks(user.getId())).thenReturn(Optional.of(List.of(new Link("link"))));

        Command command = new ListCommand(userObjectProvider, repository);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo("Список отслеживаемых ссылок:\nlink\n");
    }

    @Test
    public void userNotExistListCommandTest() {
        when(repository.getLinks(user.getId())).thenReturn(Optional.empty());

        Command command = new ListCommand(userObjectProvider, repository);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу с ботом");
    }

    @Test
    public void noLinksListCommandTest() {
        when(repository.getLinks(user.getId())).thenReturn(Optional.of(List.of()));

        Command command = new ListCommand(userObjectProvider, repository);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo("Сейчас вы не отслеживаете никаких ссылок");
    }

    @Test
    public void resetCommandTest() {
        when(repository.deleteUser(user.getId())).thenReturn(UserRepository.Result.OK);

        Command command = new ResetCommand(userObjectProvider, repository);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы удалили все записи из бота. Чтобы снова начать работу с ботом, введите /start");
    }

    @Test
    public void userNotExistResetCommandTest() {
        when(repository.deleteUser(user.getId())).thenReturn(UserRepository.Result.USER_NOT_EXIST);

        Command command = new ResetCommand(userObjectProvider, repository);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы и так не были зарегестрированы в боте.");
    }

    @Test
    public void startCommandTest() {
        when(repository.register(user.getId())).thenReturn(UserRepository.Result.OK);

        Command command = new StartCommand(userObjectProvider, repository);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы успешно запустили бота. Теперь вы можете отслеживать ссылки. "
                + "Чтобы узнать больше, используйте команду /help"
        );
    }

    @Test
    public void userAlreadyExistStartCommandTest() {
        when(repository.register(user.getId())).thenReturn(UserRepository.Result.USER_ALREADY_EXIST);

        Command command = new StartCommand(userObjectProvider, repository);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы уже зарегестрированы в боте. Чтобы сбросить ссылки отправьте команду /reset");
    }

    @Test
    public void trackCommandTest() {
        when(repository.addLink(eq(user.getId()), any(Link.class))).thenReturn(UserRepository.Result.OK);
        when(parser.parse(any(String.class))).thenReturn(Optional.of(mock(Link.class)));
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));

        Command command = new TrackCommand(userObjectProvider, repository, parser);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы начали отслеживание сайта. Теперь при обновлении контента по ссылке, вы получите уведомление");
    }

    @Test
    public void userNotExistTrackCommandTest() {
        when(repository.addLink(eq(user.getId()), any(Link.class))).thenReturn(UserRepository.Result.USER_NOT_EXIST);
        when(parser.parse(any(String.class))).thenReturn(Optional.of(mock(Link.class)));
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));

        Command command = new TrackCommand(userObjectProvider, repository, parser);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу с ботом");
    }

    @Test
    public void linkAlreadyExistTrackCommandTest() {
        when(repository.addLink(eq(user.getId()), any(Link.class))).thenReturn(UserRepository.Result.LINK_ALREADY_EXIST);
        when(parser.parse(any(String.class))).thenReturn(Optional.of(mock(Link.class)));
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));

        Command command = new TrackCommand(userObjectProvider, repository, parser);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы уже отслеживаете эту ссылку. Введите /list, чтобы увидеть все отслеживаемые ссылки");
    }

    @Test
    public void noLinkTrackCommandTest() {
        when(parser.getUri(any(Update.class))).thenReturn(Optional.empty());

        Command command = new TrackCommand(userObjectProvider, repository, parser);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Чтобы начать отслеживать ресурс, отправьте /track и ссылку на этот ресурс");
    }

    @Test
    public void notSupportedLinkTrackCommandTest() {
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));
        when(parser.parse(any(String.class))).thenReturn(Optional.empty());

        Command command = new TrackCommand(userObjectProvider, repository, parser);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Данный ресурс, к сожалению, пока не поддерживается");
    }

    @Test
    public void untrackCommandTest() {
        when(repository.deleteLink(eq(user.getId()), any(Link.class))).thenReturn(UserRepository.Result.OK);
        when(parser.parse(any(String.class))).thenReturn(Optional.of(mock(Link.class)));
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));

        Command command = new UntrackCommand(userObjectProvider, repository, parser);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo("Вы прекратили отслеживание ссылки");
    }

    @Test
    public void userNotExistUntrackCommandTest() {
        when(repository.deleteLink(eq(user.getId()), any(Link.class))).thenReturn(UserRepository.Result.USER_NOT_EXIST);
        when(parser.parse(any(String.class))).thenReturn(Optional.of(mock(Link.class)));
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));

        Command command = new UntrackCommand(userObjectProvider, repository, parser);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу");
    }

    @Test
    public void linkNotExistUntrackCommandTest() {
        when(repository.deleteLink(eq(user.getId()), any(Link.class))).thenReturn(UserRepository.Result.LINK_NOT_EXIST);
        when(parser.parse(any(String.class))).thenReturn(Optional.of(mock(Link.class)));
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));

        Command command = new UntrackCommand(userObjectProvider, repository, parser);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы не отслеживали эту ссылку. Введите /list, чтобы увидеть все отслеживаемые ссылки");
    }

    @Test
    public void noLinkUntrackCommandTest() {
        when(parser.getUri(any(Update.class))).thenReturn(Optional.empty());

        Command command = new UntrackCommand(userObjectProvider, repository, parser);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Чтобы прекратить отслеживать ресурс, отправьте /untrack и ссылку на этот ресурс");
    }

    @Test
    public void notSupportedLinkUntrackCommandTest() {
        when(parser.getUri(any(Update.class))).thenReturn(Optional.of("uri"));
        when(parser.parse(any(String.class))).thenReturn(Optional.empty());

        Command command = new UntrackCommand(userObjectProvider, repository, parser);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Данный ресурс, к сожалению, пока не поддерживается");
    }

    @Test
    public void simpleTextFailCommandTest() {
        Command command = new SimpleTextFailCommand(userObjectProvider);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
    }

    @Test
    public void unknownFailCommand() {
        Command command = new UnknownFailCommand(userObjectProvider);
        command.execute(update);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
    }

}

