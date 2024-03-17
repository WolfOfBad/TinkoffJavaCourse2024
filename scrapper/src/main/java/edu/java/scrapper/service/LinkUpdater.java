package edu.java.scrapper.service;

import edu.java.scrapper.domain.dto.Link;
import java.time.OffsetDateTime;

public interface LinkUpdater {
    void update(Link link);

    void updateAll();

    void updateOldLinks(OffsetDateTime oldLinkTime);
}
