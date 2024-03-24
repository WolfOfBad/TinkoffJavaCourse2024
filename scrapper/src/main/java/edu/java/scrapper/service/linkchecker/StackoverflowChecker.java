package edu.java.scrapper.service.linkchecker;

import edu.java.scrapper.client.stackoverflow.StackoverflowClient;
import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StackoverflowChecker extends LinkCheckerManager {
    private StackoverflowClient client;
    private static final Pattern PATTERN = Pattern.compile("https://stackoverflow\\.com/questions/([^/]*).*");

    @Override
    public OffsetDateTime check(String uri) {
        Matcher matcher = PATTERN.matcher(uri);
        if (matcher.find()) {
            long id = Long.parseLong(matcher.group(1));

            return client.getQuestion(id).items().getFirst().lastActivity();
        }
        return checkNext(uri);
    }
}
