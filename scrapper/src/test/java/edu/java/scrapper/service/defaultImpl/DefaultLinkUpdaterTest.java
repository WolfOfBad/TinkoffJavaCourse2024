package edu.java.scrapper.service.defaultImpl;

import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.integration.IntegrationTest;
import edu.java.scrapper.service.UpdateNotifier;
import edu.java.scrapper.service.impl.DefaultLinkUpdater;
import edu.java.scrapper.service.linkchecker.LinkCheckerManager;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(
    properties = {"app.database-access-type=jdbc", "app.scheduler.enable=false"}
)
@ExtendWith(MockitoExtension.class)
public class DefaultLinkUpdaterTest extends IntegrationTest {
    @Autowired
    private LinkRepository linkRepository;

    @Mock
    private LinkCheckerManager linkCheckerManager;

    @Mock
    private UpdateNotifier updateNotifier;

    @Mock
    private LinkRepository linkRepositoryMock;

    private DefaultLinkUpdater linkUpdater;

    @BeforeEach
    public void before() {
        linkUpdater = new DefaultLinkUpdater(
            linkCheckerManager,
            linkRepository,
            updateNotifier
        );
    }

    @Test
    @Transactional
    @Rollback
    public void updateTest() {
        OffsetDateTime oldTime = OffsetDateTime.parse("2020-01-01T00:00:00+00:00");
        OffsetDateTime newTime = OffsetDateTime.parse("2021-01-01T00:00:00+00:00");
        when(linkCheckerManager.check(anyString())).thenReturn(newTime);

        Link link = linkRepository.add(URI.create("test"));
        link = linkRepository.update(Link.builder()
            .uri(link.uri())
            .id(link.id())
            .lastUpdate(oldTime)
            .build());

        linkUpdater.update(link);
        Link result = linkRepository.get(URI.create("test")).get();

        assertThat(result.lastUpdate()).isEqualTo(newTime);
    }

    @Test
    public void updateAllTest() {
        DefaultLinkUpdater defaultLinkUpdater = new DefaultLinkUpdater(
            linkCheckerManager,
            linkRepositoryMock,
            updateNotifier
        );

        Link link1 = new Link(1, URI.create("test1"), OffsetDateTime.parse("2020-01-01T00:00:00+00:00"));
        Link link2 = new Link(2, URI.create("test2"), OffsetDateTime.parse("2020-02-01T00:00:00+00:00"));
        Link link3 = new Link(3, URI.create("test3"), OffsetDateTime.parse("2020-03-01T00:00:00+00:00"));
        when(linkRepositoryMock.getAll()).thenReturn(List.of(link1, link2, link3));
        when(linkCheckerManager.check(anyString())).thenReturn(OffsetDateTime.parse("2020-02-02T00:00:00+00:00"));

        defaultLinkUpdater.updateAll();

        verify(updateNotifier, times(2)).notifyUsers(any(Link.class), anyList());
    }

    @Test
    public void updateOldLinksTest() {
        DefaultLinkUpdater defaultLinkUpdater = new DefaultLinkUpdater(
            linkCheckerManager,
            linkRepositoryMock,
            updateNotifier
        );

        Link link1 = new Link(1, URI.create("test1"), OffsetDateTime.parse("2020-01-01T00:00:00+00:00"));
        Link link2 = new Link(2, URI.create("test2"), OffsetDateTime.parse("2020-02-01T00:00:00+00:00"));
        Link link3 = new Link(3, URI.create("test3"), OffsetDateTime.parse("2020-03-01T00:00:00+00:00"));
        when(linkRepositoryMock.getOldLinks(any(OffsetDateTime.class))).thenReturn(List.of(link1, link2, link3));
        when(linkCheckerManager.check(anyString())).thenReturn(OffsetDateTime.parse("2020-02-02T00:00:00+00:00"));

        defaultLinkUpdater.updateOldLinks(OffsetDateTime.parse("2020-01-02T00:00:00+00:00"));

        verify(updateNotifier, times(2)).notifyUsers(any(Link.class), anyList());
    }

}
