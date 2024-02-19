package edu.java.bot.model.link.parser;

import edu.java.bot.model.link.Link;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LinkParser {
    private LinkParser nextParse;

    public static LinkParser link(LinkParser first, LinkParser... chain) {
        LinkParser head = first;
        for (LinkParser next : chain) {
            head.nextParse = next;
            head = next;
        }

        return first;
    }

    public abstract Optional<Link> parse(String uri);

    protected Optional<Link> parseNext(String uri) {
        if (nextParse == null) {
            return Optional.empty();
        }
        return nextParse.parse(uri);
    }

    protected Optional<String> parseRegex(Pattern pattern, String uri) {
        if (uri.isEmpty()) {
            return Optional.empty();
        }

        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            String result = uri;
            if (matcher.group(1) == null) {
                result = "https://" + uri;
            }

            return Optional.of(result);
        }

        return Optional.empty();
    }

}
