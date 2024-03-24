package edu.java.scrapper.service.linkchecker;

import edu.java.scrapper.client.Event;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("MultipleStringLiterals")
public abstract class LinkCheckerManager {
    private LinkCheckerManager nextParse;

    public static LinkCheckerManager link(LinkCheckerManager first, LinkCheckerManager... chain) {
        LinkCheckerManager head = first;
        for (LinkCheckerManager next : chain) {
            head.nextParse = next;
            head = next;
        }

        return first;
    }

    public abstract OffsetDateTime check(String uri);

    public abstract Event getLastEvent(String uri);

    protected final OffsetDateTime checkNext(String uri) {
        if (nextParse == null) {
            throw new RuntimeException("Wrong link format");
        }
        return nextParse.check(uri);
    }

    protected final Event getLastEventNext(String uri) {
        if (nextParse == null) {
            throw new RuntimeException("Wrong link format");
        }
        return nextParse.getLastEvent(uri);
    }

}
