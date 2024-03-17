package edu.java.scrapper.domain.jooq;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.dto.TelegramChat;
import edu.java.scrapper.integration.IntegrationTest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    properties = {"app.database-access-type=jooq", "app.scheduler.enable=false"}
)
public class JooqLinkTest extends IntegrationTest {
    @Autowired
    public LinkRepository linkRepository;

    @Autowired
    public ChatRepository chatRepository;

    @Autowired
    public JdbcClient client;

    @Test
    @Transactional
    @Rollback
    public void getAllTest() {
        Link link1 = linkRepository.add(URI.create("1"));
        Link link2 = linkRepository.add(URI.create("2"));
        Link link3 = linkRepository.add(URI.create("3"));

        List<Link> result = linkRepository.getAll();

        assertThat(result).asList().isEqualTo(List.of(link1, link2, link3));
    }

    @Test
    @Transactional
    @Rollback
    public void getOldLinksTest() {
        Link link1 = linkRepository.add(URI.create("1"));
        Link link2 = linkRepository.add(URI.create("2"));

        link1 = linkRepository.update(new Link(
            link1.id(),
            link1.uri(),
            OffsetDateTime.parse("2020-01-01T00:00:00+00:00")
        ));
        link2 = linkRepository.update(new Link(
            link2.id(),
            link2.uri(),
            OffsetDateTime.parse("2021-01-01T00:00:00+00:00")
        ));

        List<Link> result = linkRepository.getOldLinks(OffsetDateTime.parse("2020-02-01T00:00:00+00:00"));

        assertThat(result).asList().isEqualTo(List.of(link1));
    }

    @Test
    @Transactional
    @Rollback
    public void getTest() {
        Link link = linkRepository.add(URI.create("1"));

        Optional<Link> result = linkRepository.get(link.uri());

        assertThat(result.get()).isEqualTo(link);
    }

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        URI uri = URI.create("1");

        Link link = linkRepository.add(uri);
        link = new Link(link.id(), link.uri(), link.lastUpdate().toInstant().atOffset(ZoneOffset.UTC));
        Link result = client.sql("select * from link where uri = :uri")
            .param("uri", uri.toString())
            .query(Link.class)
            .single();

        assertThat(result).isEqualTo(link);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteTest() {
        Link link = linkRepository.add(URI.create("1"));

        Optional<Link> beforeDelete = linkRepository.get(link.uri());
        linkRepository.delete(link.uri());
        Optional<Link> afterDelete = linkRepository.get(link.uri());

        assertThat(beforeDelete).isPresent();
        assertThat(afterDelete).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void updateTest() {
        Link link = linkRepository.add(URI.create("1"));

        Link result = linkRepository.update(new Link(
            link.id(),
            link.uri(),
            OffsetDateTime.parse("2020-02-01T00:00:00+00:00")
        ));
        Optional<Link> getResult = linkRepository.get(link.uri());

        assertThat(result).isEqualTo(getResult.get());
    }

    @Test
    @Transactional
    @Rollback
    public void subscribeTest() {
        TelegramChat chat = chatRepository.add(123);
        Link link = linkRepository.add(URI.create("test"));

        linkRepository.subscribe(link.uri(), chat.tgChatId());

        Subscription subscription = client.sql("select * from chat_link")
            .query(Subscription.class)
            .single();

        assertThat(subscription.chatId).isEqualTo(chat.id());
        assertThat(subscription.linkId).isEqualTo(link.id());
    }

    @Test
    @Transactional
    @Rollback
    public void unsubscribeTest() {
        TelegramChat chat = chatRepository.add(123);
        Link link = linkRepository.add(URI.create("test"));

        linkRepository.subscribe(link.uri(), chat.tgChatId());

        Optional<Subscription> beforeDelete = client.sql("select * from chat_link")
            .query(Subscription.class)
            .optional();
        linkRepository.unsubscribe(link.uri(), chat.tgChatId());
        Optional<Subscription> afterDelete = client.sql("select * from chat_link")
            .query(Subscription.class)
            .optional();

        assertThat(beforeDelete).isPresent();
        assertThat(afterDelete).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void getAllByIdTest() {
        TelegramChat chat = chatRepository.add(123);
        Link link1 = linkRepository.add(URI.create("1"));
        Link link2 = linkRepository.add(URI.create("2"));
        linkRepository.subscribe(link1.uri(), chat.tgChatId());

        List<Link> result = linkRepository.getAllById(chat.tgChatId());

        assertThat(result).asList().isEqualTo(List.of(link1));
    }

    @Test
    @Transactional
    @Rollback
    public void addAndSubscribeTest() {
        TelegramChat chat = chatRepository.add(123);

        Link link = linkRepository.addAndSubscribe(URI.create("1"), chat.tgChatId());

        Subscription subscription = client.sql("select * from chat_link")
            .query(Subscription.class)
            .single();

        assertThat(subscription.linkId).isEqualTo(link.id());
        assertThat(subscription.chatId).isEqualTo(chat.id());
    }

    @Test
    @Transactional
    @Rollback
    public void getUsersTest() {
        Link link = linkRepository.add(URI.create("test"));

        TelegramChat chat1 = chatRepository.add(1);
        TelegramChat chat2 = chatRepository.add(2);

        linkRepository.subscribe(link.uri(), chat1.tgChatId());
        linkRepository.subscribe(link.uri(), chat2.tgChatId());

        List<TelegramChat> result = linkRepository.getUsers(link.uri());

        assertThat(result).asList().isEqualTo(List.of(chat1, chat2));
    }

    private record Subscription(
        long chatId,
        long linkId
    ) {
    }

}
