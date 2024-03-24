package edu.java.scrapper.service.impl;

import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.UpdateNotifier;
import edu.java.scrapper.service.linkchecker.LinkCheckerManager;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class DefaultLinkUpdater implements LinkUpdater {
    private LinkCheckerManager linkCheckerManager;
    private LinkRepository linkRepository;
    private UpdateNotifier updateNotifier;

    @Transactional
    @Override
    public void update(Link link) {
        OffsetDateTime lastUpdate = linkCheckerManager.check(link.uri().toString());
        if (link.lastUpdate().isBefore(lastUpdate)) {
            linkRepository.update(Link.builder()
                .id(link.id())
                .uri(link.uri())
                .lastUpdate(lastUpdate)
                .build());

            updateNotifier.notifyUsers(link, linkRepository.getUsers(link.uri()));
        }
    }

    @Transactional
    @Override
    public void updateAll() {
        // update all links in database
        List<Link> links = linkRepository.getAll();
        for (Link link : links) {
            update(link);
        }
    }

    @Transactional
    @Override
    public void updateOldLinks(OffsetDateTime oldLinkTime) {
        // update only old links
        List<Link> links = linkRepository.getOldLinks(oldLinkTime);
        for (Link link : links) {
            update(link);
        }
    }
}
