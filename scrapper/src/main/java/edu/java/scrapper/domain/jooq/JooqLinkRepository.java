package edu.java.scrapper.domain.jooq;

import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.dto.TelegramChat;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.scrapper.domain.jooq.generated.Tables.CHAT;
import static edu.java.scrapper.domain.jooq.generated.Tables.CHAT_LINK;
import static edu.java.scrapper.domain.jooq.generated.Tables.LINK;

@AllArgsConstructor
public class JooqLinkRepository implements LinkRepository {
    private DSLContext dslContext;

    @Override
    public List<Link> getAll() {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .fetchInto(Link.class);
    }

    @Override
    public List<Link> getOldLinks(OffsetDateTime time) {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .where(LINK.LAST_UPDATE.lessOrEqual(time))
            .fetchInto(Link.class);
    }

    @Override
    public List<Link> getAllById(long tgChatId) {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .join(CHAT_LINK).on(LINK.ID.eq(CHAT_LINK.LINK_ID))
            .join(CHAT).on(CHAT.ID.eq(CHAT_LINK.CHAT_ID))
            .fetchInto(Link.class);
    }

    @Override
    public Optional<Link> get(URI uri) {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .where(LINK.URI.eq(uri.toString()))
            .fetchOptionalInto(Link.class);
    }

    @Override
    public Link add(URI uri) {
        return dslContext.insertInto(LINK)
            .columns(LINK.URI, LINK.LAST_UPDATE)
            .values(uri.toString(), OffsetDateTime.now())
            .returning(LINK.fields())
            .fetchInto(Link.class)
            .getFirst();
    }

    @Override
    public Link addAndSubscribe(URI uri, long tgChatId) {
        Link link = add(uri);

        dslContext.insertInto(CHAT_LINK)
            .columns(CHAT_LINK.LINK_ID, CHAT_LINK.CHAT_ID)
            .values(
                link.id(),
                dslContext.select(CHAT.ID)
                    .from(CHAT)
                    .where(CHAT.TG_CHAT_ID.eq(tgChatId))
                    .fetchInto(Long.class)
                    .getFirst()
            ).execute();

        return link;
    }

    @Override
    public Link delete(URI uri) {
        return dslContext.deleteFrom(LINK)
            .where(LINK.URI.eq(uri.toString()))
            .returning(LINK.fields())
            .fetchInto(Link.class)
            .getFirst();
    }

    @Override
    public Link update(Link link) {
        return dslContext.update(LINK)
            .set(LINK.LAST_UPDATE, link.lastUpdate())
            .where(LINK.ID.eq(link.id()))
            .returning(LINK.fields())
            .fetchInto(Link.class)
            .getFirst();
    }

    @Override
    public Link subscribe(URI uri, long tgChatId) {
        long linkId = dslContext.select(LINK.ID)
            .from(LINK)
            .where(LINK.URI.eq(uri.toString()))
            .fetchInto(Long.class)
            .getFirst();

        long chatId = dslContext.select(CHAT.ID)
            .from(CHAT)
            .where(CHAT.TG_CHAT_ID.eq(tgChatId))
            .fetchInto(Long.class)
            .getFirst();

        dslContext.insertInto(CHAT_LINK)
            .columns(CHAT_LINK.LINK_ID, CHAT_LINK.CHAT_ID)
            .values(linkId, chatId)
            .execute();

        return get(uri).orElse(null);
    }

    @Override
    public Link unsubscribe(URI uri, long tgChatId) {
        Link link = get(uri).orElse(null);

        dslContext.deleteFrom(CHAT_LINK)
            .where(CHAT_LINK.LINK_ID.eq(link.id()))
            .and(CHAT_LINK.CHAT_ID.eq(
                dslContext.select(CHAT.ID)
                    .from(CHAT)
                    .where(CHAT.TG_CHAT_ID.eq(tgChatId))
            ))
            .execute();

        if (getUsers(uri).isEmpty()) {
            delete(uri);
        }

        return link;
    }

    @Override
    public List<TelegramChat> getUsers(URI uri) {
        return dslContext.select(CHAT.fields())
            .from(CHAT)
            .join(CHAT_LINK).on(CHAT.ID.eq(CHAT_LINK.CHAT_ID))
            .join(LINK).on(LINK.ID.eq(CHAT_LINK.LINK_ID))
            .where(LINK.URI.eq(uri.toString()))
            .fetchInto(TelegramChat.class);
    }
}
