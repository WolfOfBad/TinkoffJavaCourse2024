package edu.java.bot.model.link.parser;

import edu.java.bot.model.link.Link;
import java.net.URI;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;

@AllArgsConstructor
public class StackOverflowParser extends LinkParser {
    private static final Pattern PATTERN = Pattern.compile("(https://)?stackoverflow\\.com/.*");
    private final ApplicationContext context;

    @Override
    public Optional<Link> parse(String uri) {
        Optional<String> link = parseRegex(PATTERN, uri);

        return link.map(s -> context.getBean(Link.class, URI.create(s))).or(() -> parseNext(uri));
    }
}
