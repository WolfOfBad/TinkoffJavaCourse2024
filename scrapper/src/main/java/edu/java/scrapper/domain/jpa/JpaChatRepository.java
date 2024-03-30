package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.domain.jpa.entity.ChatEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

public interface JpaChatRepository extends CrudRepository<ChatEntity, Long> {
    @EntityGraph(attributePaths = {"links"})
    Optional<ChatEntity> findByTgChatId(long tgChatId);

}
