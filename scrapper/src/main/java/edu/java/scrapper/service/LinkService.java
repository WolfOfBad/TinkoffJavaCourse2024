package edu.java.scrapper.service;

import edu.java.scrapper.domain.dto.Link;
import java.net.URI;
import java.util.List;

public interface LinkService {
    Link add(long tgChatId, URI uri);

    Link remove(long tgChatId, URI uri);

    List<Link> allLinks(long tgChatId);
}
