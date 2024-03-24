package edu.java.scrapper.service.linkchecker;

import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;

@Service
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

    protected final OffsetDateTime checkNext(String uri) {
        if (nextParse == null) {
            throw new RuntimeException("Wrong link format");
        }
        return nextParse.check(uri);
    }

}
