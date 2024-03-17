package edu.java.scrapper.domain;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.dto.TelegramChat;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    List<Link> getAll();

    List<Link> getOldLinks(OffsetDateTime time);

    List<Link> getAllById(long tgChatId);

    Optional<Link> get(URI uri);

    Link add(URI uri);

    Link addAndSubscribe(URI uri, long tgChatId);

    Link delete(URI uri);

    Link update(Link link);

    Link subscribe(URI uri, long tgChatId);

    Link unsubscribe(URI uri, long tgChatId);

    List<TelegramChat> getUsers(URI uri);
}
