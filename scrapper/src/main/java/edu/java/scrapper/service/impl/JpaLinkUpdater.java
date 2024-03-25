package edu.java.scrapper.service.impl;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.dto.TelegramChat;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.jpa.entity.LinkEntity;
import edu.java.scrapper.exception.NoSuchLinkException;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.UpdateNotifier;
import edu.java.scrapper.service.linkchecker.LinkCheckerManager;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class JpaLinkUpdater implements LinkUpdater {
    private LinkCheckerManager linkCheckerManager;
    private JpaLinkRepository linkRepository;
    private UpdateNotifier updateNotifier;

    @Override
    @Transactional
    public void update(Link link) {
        OffsetDateTime lastUpdate = linkCheckerManager.check(link.uri().toString());
        if (link.lastUpdate().isBefore(lastUpdate)) {
            LinkEntity linkEntity = linkRepository.getByUri(link.uri().toString())
                .orElseThrow(() -> new NoSuchLinkException("Link does not exist"));

            updateNotifier.notifyUsers(
                link,
                linkEntity.getChats().stream()
                    .map(entity -> TelegramChat.builder()
                        .id(entity.getId())
                        .tgChatId(entity.getTgChatId())
                        .build())
                    .toList(),
                linkCheckerManager.getLastEvent(link.uri().toString()).getMessage()
            );

            linkEntity.setLastUpdate(lastUpdate);
            linkRepository.save(linkEntity);
        }
    }

    @Override
    @Transactional
    public void updateAll() {
        Iterable<LinkEntity> linkEntityList = linkRepository.findAll();
        for (LinkEntity link : linkEntityList) {
            update(Link.builder()
                .uri(URI.create(link.getUri()))
                .lastUpdate(link.getLastUpdate())
                .id(link.getId())
                .build());
        }
    }

    @Override
    @Transactional
    public void updateOldLinks(OffsetDateTime oldLinkTime) {
        List<LinkEntity> linkEntityList = linkRepository.findByLastUpdateLessThanEqual(oldLinkTime);
        for (LinkEntity link : linkEntityList) {
            update(Link.builder()
                .uri(URI.create(link.getUri()))
                .lastUpdate(link.getLastUpdate())
                .id(link.getId())
                .build());
        }
    }
}
