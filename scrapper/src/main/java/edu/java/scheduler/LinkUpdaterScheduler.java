package edu.java.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "#{@intervalDelay}")
    public void update() {
        log.info("Updating info...");
    }

}
