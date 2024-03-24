package edu.java.scrapper.service.impl;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.exception.UserAlreadyRegisteredException;
import edu.java.scrapper.service.TgChatService;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class DefaultTgChatService implements TgChatService {
    private ChatRepository chatRepository;
    private LinkRepository linkRepository;

    @Transactional
    @Override
    public void register(long tgChatId) {
        try {
            chatRepository.add(tgChatId);
        } catch (DuplicateKeyException exception) {
            throw new UserAlreadyRegisteredException(exception.getMessage(), exception);
        }
    }

    @Transactional
    @Override
    public void unregister(long tgChatId) {
        try {
            for (Link link : linkRepository.getAllById(tgChatId)) {
                linkRepository.unsubscribe(link.uri(), tgChatId);
            }
            chatRepository.removeByTgChatId(tgChatId);
        } catch (EmptyResultDataAccessException | NoSuchElementException exception) {
            throw new NoSuchChatException(exception.getMessage(), exception);
        }

    }
}
