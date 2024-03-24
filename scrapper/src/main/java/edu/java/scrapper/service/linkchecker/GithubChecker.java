package edu.java.scrapper.service.linkchecker;

import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.client.github.dto.event.EventDTO;
import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GithubChecker extends LinkCheckerManager {
    private GithubClient client;
    private static final Pattern PATTERN = Pattern.compile("https://github\\.com/([^/]*)/([^/]*).*");

    @Override
    public OffsetDateTime check(String uri) {
        Matcher matcher = PATTERN.matcher(uri);
        if (matcher.find()) {
            String user = matcher.group(1);
            String repository = matcher.group(2);

            return client.getRepository(user, repository).pushedAt();
        }
        return checkNext(uri);
    }

    @Override
    public EventDTO getLastEvent(String uri) {
        Matcher matcher = PATTERN.matcher(uri);
        if (matcher.find()) {
            String user = matcher.group(1);
            String repository = matcher.group(2);

            return client.getEvent(user, repository);
        }
        return getLastEventNext(uri);
    }
}
