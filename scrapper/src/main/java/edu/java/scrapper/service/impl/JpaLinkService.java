package edu.java.scrapper.service.impl;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.jpa.entity.ChatEntity;
import edu.java.scrapper.domain.jpa.entity.LinkEntity;
import edu.java.scrapper.exception.AlreadySubscribedException;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.exception.NoSuchLinkException;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdater;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@SuppressWarnings("MultipleStringLiterals")
public class JpaLinkService implements LinkService {
    private JpaChatRepository chatRepository;

    private JpaLinkRepository linkRepository;

    private LinkUpdater linkUpdater;

    @Override
    @Transactional
    public Link add(long tgChatId, URI uri) {
        ChatEntity chat = chatRepository.getByTgChatId(tgChatId)
            .orElseThrow(() -> new NoSuchChatException("Chat does not exist"));
        LinkEntity link = getOrCreateLink(uri);

        if (chat.getLinks().contains(link)) {
            throw new AlreadySubscribedException("User already subscribed to this link");
        }

        chat.getLinks().add(link);
        chatRepository.saveAndFlush(chat);

        return convertToLinkDto(link);
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, URI uri) {
        ChatEntity chat = chatRepository.getByTgChatId(tgChatId)
            .orElseThrow(() -> new NoSuchChatException("Chat does not exist"));
        LinkEntity link = linkRepository.getByUri(uri.toString())
            .orElseThrow(() -> new NoSuchLinkException("Link does not exist"));

        chat.getLinks().remove(link);
        if (link.getChats().isEmpty()) {
            linkRepository.delete(link);
        }

        return convertToLinkDto(link);
    }

    @Override
    @Transactional
    public List<Link> allLinks(long tgChatId) {
        ChatEntity chat = chatRepository.getByTgChatId(tgChatId)
            .orElseThrow(() -> new NoSuchChatException("Chat does not exist"));

        return chat.getLinks().stream()
            .map(this::convertToLinkDto)
            .toList();
    }

    private LinkEntity getOrCreateLink(URI uri) {
        Optional<LinkEntity> link = linkRepository.getByUri(uri.toString());

        if (link.isEmpty()) {
            LinkEntity entity = new LinkEntity();
            entity.setUri(uri.toString());
            entity.setLastUpdate(OffsetDateTime.now());

            return linkRepository.save(entity);
        }

        linkUpdater.update(convertToLinkDto(link.get()));
        return link.get();
    }

    private Link convertToLinkDto(LinkEntity link) {
        return Link.builder()
            .id(link.getId())
            .lastUpdate(link.getLastUpdate())
            .uri(URI.create(link.getUri()))
            .build();
    }

}
