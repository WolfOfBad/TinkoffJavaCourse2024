package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.domain.jpa.entity.LinkEntity;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface JpaLinkRepository extends CrudRepository<LinkEntity, Long> {
    Optional<LinkEntity> findByUri(URI uri);

    List<LinkEntity> findByLastUpdateLessThanEqual(OffsetDateTime lastUpdate);

}
