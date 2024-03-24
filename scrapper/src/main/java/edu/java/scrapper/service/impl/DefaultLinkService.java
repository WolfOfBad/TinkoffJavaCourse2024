package edu.java.scrapper.service.impl;

import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.exception.AlreadySubscribedException;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.exception.NoSuchLinkException;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdater;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class DefaultLinkService implements LinkService {
    private LinkRepository linkRepository;
    private LinkUpdater linkUpdater;

    @Transactional
    @Override
    public Link add(long tgChatId, URI uri) {
        try {
            Optional<Link> link = linkRepository.get(uri);

            if (link.isPresent()) {
                linkUpdater.update(link.get());
                return linkRepository.subscribe(uri, tgChatId);
            }

            return linkRepository.addAndSubscribe(uri, tgChatId);
        } catch (DuplicateKeyException exception) {
            throw new AlreadySubscribedException(exception.getMessage(), exception);
        } catch (DataIntegrityViolationException | NoSuchElementException exception) {
            throw new NoSuchChatException(exception.getMessage(), exception);
        }
    }

    @Transactional
    @Override
    public Link remove(long tgChatId, URI uri) {
        try {
            return linkRepository.unsubscribe(uri, tgChatId);
        } catch (EmptyResultDataAccessException | NullPointerException exception) {
            throw new NoSuchLinkException(exception.getMessage(), exception);
        }

    }

    @Transactional
    @Override
    public List<Link> allLinks(long tgChatId) {
        return linkRepository.getAllById(tgChatId);
    }
}
