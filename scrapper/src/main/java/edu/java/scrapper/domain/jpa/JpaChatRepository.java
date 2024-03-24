package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.domain.jpa.entity.ChatEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<ChatEntity, Long> {
    @EntityGraph(attributePaths = {"links"})
    Optional<ChatEntity> getByTgChatId(long tgChatId);

}
