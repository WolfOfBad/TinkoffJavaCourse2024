package edu.java.scrapper.scheduler;

import edu.java.scrapper.configuration.ApplicationConfigProperties;
import edu.java.scrapper.service.LinkUpdater;
import java.time.Duration;
import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "app.scheduler.enable", matchIfMissing = true)
@Slf4j
public class LinkUpdaterScheduler {
    private final LinkUpdater linkUpdater;
    private final Duration oldLinkTime;

    public LinkUpdaterScheduler(LinkUpdater linkUpdater, ApplicationConfigProperties.Scheduler scheduler) {
        this.linkUpdater = linkUpdater;
        this.oldLinkTime = scheduler.oldLinkTime();
    }

    @Scheduled(fixedDelayString = "#{scheduler.interval}")
    public void update() {
        log.info("Updating old links info...");
        linkUpdater.updateOldLinks(OffsetDateTime.now().minus(oldLinkTime));
    }

    @Scheduled(fixedDelayString = "#{scheduler.forceCheckDelay()}")
    public void updateAllLinks() {
        log.info("Updating all info...");
        linkUpdater.updateAll();
    }

}
