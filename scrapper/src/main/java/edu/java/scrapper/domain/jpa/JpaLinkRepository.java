package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.domain.jpa.entity.LinkEntity;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {
    Optional<LinkEntity> getByUri(String uri);

    List<LinkEntity> findByLastUpdateLessThanEqual(OffsetDateTime lastUpdate);

}
