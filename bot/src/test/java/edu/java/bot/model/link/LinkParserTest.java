package edu.java.bot.model.link;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.link.parser.GithubParser;
import edu.java.bot.model.link.parser.LinkParserManager;
import edu.java.bot.model.link.parser.StackOverflowParser;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinkParserTest {
    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private MessageEntity entity;

    @Mock
    private ObjectProvider<Link> linkObjectProvider;

    @ParameterizedTest
    @MethodSource("args")
    public void parseTest(String string) {
        when(linkObjectProvider.getObject(any(String.class))).thenReturn(mock(Link.class));

        LinkParserManager parser = LinkParserManager.link(
            new GithubParser(linkObjectProvider),
            new StackOverflowParser(linkObjectProvider)
        );
        Optional<Link> result = parser.parse(string);

        assertThat(result).isPresent();
    }

    private static Arguments[] args() {
        return new Arguments[] {
            Arguments.of("github.com/"),
            Arguments.of("https://github.com/"),
            Arguments.of("stackoverflow.com/"),
            Arguments.of("https://stackoverflow.com/")
        };
    }

    @Test
    public void failedParseTest() {
        String string = "somewebsite.com";

        LinkParserManager parser = LinkParserManager.link(
            new GithubParser(linkObjectProvider),
            new StackOverflowParser(linkObjectProvider)
        );
        Optional<Link> result = parser.parse(string);

        assertThat(result).isEmpty();
    }

    @Test
    public void emptyStringParseTest() {
        String string = "";

        LinkParserManager parser = LinkParserManager.link(
            new GithubParser(linkObjectProvider),
            new StackOverflowParser(linkObjectProvider)
        );
        Optional<Link> result = parser.parse(string);

        assertThat(result).isEmpty();
    }

    @Test
    public void getUriTest() {
        when(update.message()).thenReturn(message);

        when(message.entities()).thenReturn(new MessageEntity[] {entity});
        when(message.text()).thenReturn("/track uri");

        when(entity.type()).thenReturn(MessageEntity.Type.bot_command);
        when(entity.length()).thenReturn(6);

        LinkParserManager parser = LinkParserManager.link(
            new GithubParser(linkObjectProvider),
            new StackOverflowParser(linkObjectProvider)
        );

        Optional<String> uri = parser.getUri(update);

        assertThat(uri).isPresent();
    }

    @Test
    public void getUriEmptyTest() {
        when(update.message()).thenReturn(message);

        when(message.entities()).thenReturn(new MessageEntity[] {entity});
        when(message.text()).thenReturn("/track");

        when(entity.type()).thenReturn(MessageEntity.Type.bot_command);
        when(entity.length()).thenReturn(6);

        LinkParserManager parser = LinkParserManager.link(
            new GithubParser(linkObjectProvider),
            new StackOverflowParser(linkObjectProvider)
        );

        Optional<String> uri = parser.getUri(update);

        assertThat(uri).isEmpty();
    }

    @Test
    public void getUriNotCommandTest() {
        when(update.message()).thenReturn(message);

        when(message.entities()).thenReturn(new MessageEntity[] {entity});

        LinkParserManager parser = LinkParserManager.link(
            new GithubParser(linkObjectProvider),
            new StackOverflowParser(linkObjectProvider)
        );

        Optional<String> uri = parser.getUri(update);

        assertThat(uri).isEmpty();
    }

}

