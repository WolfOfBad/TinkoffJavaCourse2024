package edu.java.bot.model.link.parser;

import edu.java.bot.model.link.Link;
import java.net.URI;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GithubParser extends LinkParserManager {
    private static final Pattern PATTERN = Pattern.compile("(https://)?github\\.com/.*");

    @Override
    public Optional<Link> parse(String uri) {
        Optional<String> link = parseRegex(PATTERN, uri);

        return link.map((u) -> new Link(URI.create(u)))
            .or(() -> parseNext(uri));
    }
}
