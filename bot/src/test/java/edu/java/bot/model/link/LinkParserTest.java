package edu.java.bot.model.link;

import edu.java.bot.model.link.parser.GithubParser;
import edu.java.bot.model.link.parser.LinkParser;
import edu.java.bot.model.link.parser.StackOverflowParser;
import java.net.URI;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@ExtendWith(MockitoExtension.class)
public class LinkParserTest {
    @Mock
    private ApplicationContext context;

    @BeforeEach
    public void before() {
        lenient().when(context.getBean(eq(Link.class), any(URI.class))).thenReturn(mock(Link.class));
    }

    @ParameterizedTest
    @MethodSource("args")
    public void parseTest(String string) {
        LinkParser parser = LinkParser.link(
            new GithubParser(context),
            new StackOverflowParser(context)
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

        LinkParser parser = LinkParser.link(
            new GithubParser(context),
            new StackOverflowParser(context)
        );
        Optional<Link> result = parser.parse(string);

        assertThat(result).isEmpty();
    }

    @Test
    public void emptyStringParseTest() {
        String string = "";

        LinkParser parser = LinkParser.link(
            new GithubParser(context),
            new StackOverflowParser(context)
        );
        Optional<Link> result = parser.parse(string);

        assertThat(result).isEmpty();
    }

}
