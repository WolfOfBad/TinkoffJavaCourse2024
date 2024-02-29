package edu.java.bot.model.link.parser;

import edu.java.bot.model.link.Link;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StackOverflowParser extends LinkParserManager {
    private final ObjectProvider<Link> linkObjectProvider;
    private static final Pattern PATTERN = Pattern.compile("(https://)?stackoverflow\\.com/.*");

    @Override
    public Optional<Link> parse(String uri) {
        Optional<String> link = parseRegex(PATTERN, uri);

        return link.map(linkObjectProvider::getObject)
            .or(() -> parseNext(uri));
    }

}
