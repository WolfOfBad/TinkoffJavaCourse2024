package edu.java.bot.model.link.parser;

import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.link.Link;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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

    public Optional<String> getUri(Update update) {
        Optional<MessageEntity> entity = Arrays.stream(update.message().entities())
            .filter(e -> e.type() == MessageEntity.Type.bot_command)
            .findFirst();

        if (entity.isEmpty()) {
            return Optional.empty();
        }

        int length = entity.get().length();
        if (update.message().text().length() <= length + 1) {
            return Optional.empty();
        }

        return Optional.of(update.message().text().substring(length + 1));
    }
}
