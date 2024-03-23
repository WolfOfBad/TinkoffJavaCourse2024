package edu.java.scrapper.service.defaultImpl;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.dto.TelegramChat;
import edu.java.scrapper.exception.AlreadySubscribedException;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.exception.NoSuchLinkException;
import edu.java.scrapper.integration.IntegrationTest;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.impl.DefaultLinkService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestPropertySource(properties = {"app.database-access-type=jdbc"})
@ExtendWith(MockitoExtension.class)
public class DefaultLinkServiceTest extends IntegrationTest {
    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private JdbcClient jdbcClient;

    @Mock
    private LinkUpdater linkUpdater;

    private DefaultLinkService linkService;

    @BeforeEach
    public void before() {
        linkService = new DefaultLinkService(
            linkRepository,
            linkUpdater
        );
    }

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        TelegramChat chat = chatRepository.add(123);
        Link link = linkService.add(123, URI.create("test"));

        Optional<Subscription> subscription = Subscription.getSubscription(link.id(), chat.id(), jdbcClient);

        assertThat(subscription).isPresent();
        assertThat(subscription.get().chatId).isEqualTo(chat.id());
        assertThat(subscription.get().linkId).isEqualTo(link.id());
    }

    @Test
    @Transactional
    @Rollback
    public void multipleAddTest() {
        chatRepository.add(123);
        linkService.add(123, URI.create("test"));

        Assertions.assertThatThrownBy(() -> linkService.add(123, URI.create("test")))
            .isInstanceOf(AlreadySubscribedException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void noRegisteredAddTest() {
        Assertions.assertThatThrownBy(() -> linkService.add(123, URI.create("test")))
            .isInstanceOf(NoSuchChatException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void removeTest() {
        TelegramChat chat = chatRepository.add(123);
        Link link = linkService.add(123, URI.create("test"));

        Optional<Subscription> before = Subscription.getSubscription(link.id(), chat.id(), jdbcClient);
        linkService.remove(chat.tgChatId(), link.uri());
        Optional<Subscription> after = Subscription.getSubscription(link.id(), chat.id(), jdbcClient);

        assertThat(before).isPresent();
        assertThat(after).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void noLinkRemoveTest() {
        TelegramChat chat = chatRepository.add(123);

        assertThatThrownBy(() -> linkService.remove(chat.tgChatId(), URI.create("test")))
            .isInstanceOf(NoSuchLinkException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void allLinksTest() {
        TelegramChat chat = chatRepository.add(123);
        Link link1 = linkRepository.addAndSubscribe(URI.create("test1"), chat.tgChatId());
        Link link2 = linkRepository.add(URI.create("test2"));

        List<Link> links = linkService.allLinks(chat.tgChatId());

        assertThat(links).containsExactly(link1);
    }

    private record Subscription(
        long chatId,
        long linkId
    ) {
        public static Optional<Subscription> getSubscription(long linkId, long chatId, JdbcClient client) {
            return client.sql("select * from chat_link where link_id = ? and chat_id = ?")
                .params(linkId, chatId)
                .query(Subscription.class)
                .optional();
        }
    }

}
