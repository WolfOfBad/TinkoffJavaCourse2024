package edu.java.scrapper.service;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.dto.TelegramChat;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UpdateNotifierTest {
    @Mock
    private BotClient botClient;

    @Captor
    private ArgumentCaptor<List<Long>> tgChatIdCaptor;

    @Test
    public void notifyUsersTest() {
        UpdateNotifier updateNotifier = new UpdateNotifier(botClient);

        Link link = new Link(1, URI.create("test"), null);
        TelegramChat chat1 = new TelegramChat(1, 111);
        TelegramChat chat2 = new TelegramChat(2, 222);
        TelegramChat chat3 = new TelegramChat(3, 333);

        updateNotifier.notifyUsers(link, List.of(chat1, chat2, chat3), "string");

        verify(botClient).sendUpdate(anyLong(), anyString(), anyString(), tgChatIdCaptor.capture());

        assertThat(tgChatIdCaptor.getAllValues().getFirst()).containsExactly(111L, 222L, 333L);
    }

}
