package edu.java.scrapper.service.jpa;

import edu.java.scrapper.client.Event;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.jpa.entity.LinkEntity;
import edu.java.scrapper.integration.IntegrationTest;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.TgChatService;
import edu.java.scrapper.service.UpdateNotifier;
import edu.java.scrapper.service.impl.JpaLinkService;
import edu.java.scrapper.service.impl.JpaLinkUpdater;
import edu.java.scrapper.service.linkchecker.LinkCheckerManager;
import java.net.URI;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestPropertySource(properties = {"app.database-access-type=jpa", "app.scheduler.enable=false"})
@ExtendWith(MockitoExtension.class)
public class JpaLinkUpdaterTest extends IntegrationTest {
    private JpaLinkUpdater linkUpdater;

    private LinkService linkService;

    @Mock
    private LinkCheckerManager linkCheckerManager;

    @Autowired
    private JpaLinkRepository linkRepository;

    @Autowired
    private JpaChatRepository chatRepository;

    @Autowired
    private TgChatService chatService;

    @Mock
    private UpdateNotifier notifier;

    @BeforeEach
    public void before() {
        linkUpdater = new JpaLinkUpdater(linkCheckerManager, linkRepository, notifier);
        linkService = new JpaLinkService(chatRepository, linkRepository, linkUpdater);
    }

    @Test
    @Transactional
    @Rollback
    public void updateTest() {
        OffsetDateTime beforeTime = OffsetDateTime.parse("2020-01-01T00:00:00+00:00");
        OffsetDateTime afterTime = OffsetDateTime.parse("2021-01-01T00:00:00+00:00");
        when(linkCheckerManager.check(anyString())).thenReturn(afterTime);
        when(linkCheckerManager.getLastEvent(anyString())).thenReturn(mock(Event.class));

        LinkEntity link = new LinkEntity();
        link.setLastUpdate(beforeTime);
        link.setUri("test");
        link = linkRepository.save(link);

        linkUpdater.update(Link.builder()
            .id(link.getId())
            .uri(URI.create(link.getUri()))
            .lastUpdate(link.getLastUpdate())
            .build());

        LinkEntity result = linkRepository.findById(link.getId()).get();

        assertThat(result.getLastUpdate()).isEqualTo(afterTime);
    }

    @Test
    @Transactional
    @Rollback
    public void updateAllTest() {
        when(linkCheckerManager.check(anyString())).thenReturn(OffsetDateTime.now().plusDays(1));
        when(linkCheckerManager.getLastEvent(anyString())).thenReturn(mock(Event.class));

        chatService.register(1);
        var link1 = linkService.add(1, URI.create("test1"));
        var link2 = linkService.add(1, URI.create("test2"));

        linkUpdater.updateAll();

        verify(notifier, times(2)).notifyUsers(any(Link.class), anyList(), isNull());
    }

    @Test
    @Transactional
    @Rollback
    public void updateOldLinksTest() {
        when(linkCheckerManager.check(anyString())).thenReturn(OffsetDateTime.now().plusDays(1));
        when(linkCheckerManager.getLastEvent(anyString())).thenReturn(mock(Event.class));
        OffsetDateTime beforeTime = OffsetDateTime.parse("2020-01-01T00:00:00+00:00");
        OffsetDateTime afterTime = OffsetDateTime.parse("2021-01-01T00:00:00+00:00");

        LinkEntity link1 = new LinkEntity();
        link1.setLastUpdate(beforeTime);
        link1.setUri("test1");
        LinkEntity link2 = new LinkEntity();
        link2.setLastUpdate(afterTime);
        link2.setUri("test2");
        linkRepository.save(link1);
        linkRepository.save(link2);

        linkUpdater.updateOldLinks(beforeTime.plusDays(1));

        verify(notifier, times(1)).notifyUsers(any(Link.class), anyList(), isNull());
    }

}
