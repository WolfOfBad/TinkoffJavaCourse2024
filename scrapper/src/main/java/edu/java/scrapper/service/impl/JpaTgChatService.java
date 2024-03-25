package edu.java.scrapper.service.impl;

import edu.java.scrapper.domain.jpa.JpaChatRepository;
import edu.java.scrapper.domain.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.jpa.entity.ChatEntity;
import edu.java.scrapper.domain.jpa.entity.LinkEntity;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.exception.UserAlreadyRegisteredException;
import edu.java.scrapper.service.TgChatService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class JpaTgChatService implements TgChatService {
    private JpaChatRepository chatRepository;
    private JpaLinkRepository linkRepository;

    @Transactional
    @Override
    public void register(long tgChatId) {
        try {
            ChatEntity chat = new ChatEntity();
            chat.setTgChatId(tgChatId);

            chatRepository.save(chat);
        } catch (ConstraintViolationException exception) {
            throw new UserAlreadyRegisteredException(exception.getMessage(), exception);
        }
    }

    @Transactional
    @Override
    public void unregister(long tgChatId) {
        ChatEntity chat = chatRepository.getByTgChatId(tgChatId)
            .orElseThrow(() -> new NoSuchChatException("User not exist"));

        deleteUnsubscribedLinks(chat.getLinks());
        chatRepository.delete(chat);
    }

    @Transactional
    protected void deleteUnsubscribedLinks(List<LinkEntity> entityList) {
        for (LinkEntity link : entityList) {
            if (link.getChats().size() == 1) {
                linkRepository.delete(link);
            }
        }
    }

}
