package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.dto.TelegramChat;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;

@AllArgsConstructor
@SuppressWarnings("MultipleStringLiterals")
public class JdbcLinkRepository implements LinkRepository {
    private JdbcClient jdbcClient;

    @Override
    public List<Link> getAll() {
        String sql = "select * from link";

        return jdbcClient.sql(sql)
            .query(Link.class)
            .list();
    }

    @Override
    public List<Link> getOldLinks(OffsetDateTime time) {
        String sql = "select * from link where last_update <= :last_update";

        return jdbcClient.sql(sql)
            .param("last_update", time)
            .query(Link.class)
            .list();
    }

    @Override
    public List<Link> getAllById(long tgChatId) {
        String sql = """
            select link.id, uri, last_update from link
            join chat_link on (link.id = chat_link.link_id)
            join chat on (chat.id = chat_link.chat_id) where tg_chat_id = :tg_chat_id
            """;

        return jdbcClient.sql(sql)
            .param("tg_chat_id", tgChatId)
            .query(Link.class)
            .list();
    }

    @Override
    public Optional<Link> get(URI uri) {
        String sql = "select * from link where uri = :uri";

        return jdbcClient.sql(sql)
            .param("uri", uri.toString())
            .query(Link.class)
            .optional();
    }

    @Override
    public Link add(URI uri) {
        String addSql = "insert into link (uri, last_update) VALUES (:uri, now()) returning *";

        return jdbcClient.sql(addSql)
            .param("uri", uri.toString())
            .query(Link.class)
            .single();
    }

    @Override
    public Link addAndSubscribe(URI uri, long tgChatId) {
        Link link = add(uri);

        String subscribeSql = """
            insert into chat_link (chat_id, link_id) values
            ((select id from chat where tg_chat_id = :tg_chat_id), :link_id)
            """;

        jdbcClient.sql(subscribeSql)
            .param("tg_chat_id", tgChatId)
            .param("link_id", link.id())
            .update();

        return link;
    }

    @Override
    public Link delete(URI uri) {
        String sql = "delete from link where uri = :uri returning *";

        return jdbcClient.sql(sql)
            .param("uri", uri.toString())
            .query(Link.class)
            .single();
    }

    @Override
    public Link update(Link link) {
        String sql = "update link set last_update = :last_update where id = :id returning *";

        return jdbcClient.sql(sql)
            .param("last_update", link.lastUpdate())
            .param("id", link.id())
            .query(Link.class)
            .single();
    }

    @Override
    public Link subscribe(URI uri, long tgChatId) {
        String sql = """
            insert into chat_link (chat_id, link_id) values
            ((select id from chat where tg_chat_id = :tg_chat_id),
            (select id from link where uri = :uri))
            """;

        jdbcClient.sql(sql)
            .param("tg_chat_id", tgChatId)
            .param("uri", uri.toString())
            .update();

        return get(uri).orElse(null);
    }

    @Override
    public Link unsubscribe(URI uri, long tgChatId) {
        String sql = """
            delete from chat_link
            where chat_id = (select id from chat where tg_chat_id = :tg_chat_id)
            and link_id = (select id from link where uri = :uri)
            """;

        jdbcClient.sql(sql)
            .param("tg_chat_id", tgChatId)
            .param("uri", uri.toString())
            .update();

        Link link = get(uri).orElse(null);

        if (getUsers(uri).isEmpty()) {
            delete(uri);
        }

        return link;
    }

    @Override
    public List<TelegramChat> getUsers(URI uri) {
        String sql = """
            select chat.id, tg_chat_id from chat
            join chat_link on (chat.id = chat_link.chat_id)
            join link on (chat_link.link_id = link.id)
            where uri = :uri
            """;

        return jdbcClient.sql(sql)
            .param("uri", uri.toString())
            .query(TelegramChat.class)
            .list();
    }

}
