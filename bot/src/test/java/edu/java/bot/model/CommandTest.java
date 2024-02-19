package edu.java.bot.model;

import edu.java.bot.model.command.Command;
import edu.java.bot.model.command.impl.FailCommand;
import edu.java.bot.model.command.impl.HelpCommand;
import edu.java.bot.model.command.impl.ListCommand;
import edu.java.bot.model.command.impl.ResetCommand;
import edu.java.bot.model.command.impl.StartCommand;
import edu.java.bot.model.command.impl.TrackCommand;
import edu.java.bot.model.command.impl.UntrackCommand;
import edu.java.bot.model.link.Link;
import edu.java.bot.repository.UserRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommandTest {

    @Mock
    private User user;

    @Mock
    private UserRepository repository;

    @Test
    public void helpCommandTest() {
        Command command = new HelpCommand(user);
        command.execute();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
    }

    @Test
    public void failCommandTest() {
        Command command = new FailCommand(user, "error");
        command.execute();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo("error");
    }

    @Test
    public void listCommandTest() {
        when(repository.getLinks(user.id())).thenReturn(Optional.of(List.of(new Link(URI.create("link")))));

        Command command = new ListCommand(user, repository);
        command.execute();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo("Список отслеживаемых ссылок:\nlink\n");
    }

    @Test
    public void userNotExistListCommandTest() {
        when(repository.getLinks(user.id())).thenReturn(Optional.empty());

        Command command = new ListCommand(user, repository);
        command.execute();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу с ботом");
    }

    @Test
    public void noLinksListCommandTest() {
        when(repository.getLinks(user.id())).thenReturn(Optional.of(List.of()));

        Command command = new ListCommand(user, repository);
        command.execute();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo("Сейчас вы не отслеживаете никаких ссылок");
    }

    @Test
    public void resetCommandTest() {
        when(repository.deleteUser(user.id())).thenReturn(UserRepository.Result.OK);

        Command command = new ResetCommand(user, repository);
        command.execute();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы удалили все записи из бота. Чтобы снова начать работу с ботом, введите /start");
    }

    @Test
    public void userNotExistResetCommandTest() {
        when(repository.deleteUser(user.id())).thenReturn(UserRepository.Result.USER_NOT_EXIST);

        Command command = new ResetCommand(user, repository);
        command.execute();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы и так не были зарегестрированы в боте.");
    }

    @Test
    public void startCommandTest() {
        when(repository.register(user.id())).thenReturn(UserRepository.Result.OK);

        Command command = new StartCommand(user, repository);
        command.execute();

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
        when(repository.register(user.id())).thenReturn(UserRepository.Result.USER_ALREADY_EXIST);

        Command command = new StartCommand(user, repository);
        command.execute();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы уже зарегестрированы в боте. Чтобы сбросить ссылки отправьте команду /reset");
    }

    @Test
    public void trackCommandTest() {
        Link link = new Link(URI.create("link"));
        when(repository.addLink(user.id(), link)).thenReturn(UserRepository.Result.OK);

        Command command = new TrackCommand(user, repository, link);
        command.execute();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы начали отслеживание сайта. Теперь при обновлении контента по ссылке, вы получите уведомление");
    }

    @Test
    public void userNotExistTrackCommandTest() {
        Link link = new Link(URI.create("link"));
        when(repository.addLink(user.id(), link)).thenReturn(UserRepository.Result.USER_NOT_EXIST);

        Command command = new TrackCommand(user, repository, link);
        command.execute();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу с ботом");
    }

    @Test
    public void linkAlreadyExistTrackCommandTest() {
        Link link = new Link(URI.create("link"));
        when(repository.addLink(user.id(), link)).thenReturn(UserRepository.Result.LINK_ALREADY_EXIST);

        Command command = new TrackCommand(user, repository, link);
        command.execute();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы уже отслеживаете эту ссылку. Введите /list, чтобы увидеть все отслеживаемые ссылки");
    }

    @Test
    public void untrackCommandTest() {
        Link link = new Link(URI.create("link"));
        when(repository.deleteLink(user.id(), link)).thenReturn(UserRepository.Result.OK);

        Command command = new UntrackCommand(user, repository, link);
        command.execute();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo("Вы прекратили отслеживание ссылки");
    }

    @Test
    public void userNotExistUntrackCommandTest() {
        Link link = new Link(URI.create("link"));
        when(repository.deleteLink(user.id(), link)).thenReturn(UserRepository.Result.USER_NOT_EXIST);

        Command command = new UntrackCommand(user, repository, link);
        command.execute();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу");
    }

    @Test
    public void linkNotExistUntrackCommandTest() {
        Link link = new Link(URI.create("link"));
        when(repository.deleteLink(user.id(), link)).thenReturn(UserRepository.Result.LINK_NOT_EXIST);

        Command command = new UntrackCommand(user, repository, link);
        command.execute();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(user).sendMessage(captor.capture());

        assertThat(captor.getAllValues()).size().isEqualTo(1);
        assertThat(captor.getAllValues().getFirst()).isEqualTo(
            "Вы не отслеживали эту ссылку. Введите /list, чтобы увидеть все отслеживаемые ссылки");
    }

}
