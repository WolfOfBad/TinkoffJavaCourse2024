package edu.java.bot.model;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import java.net.URI;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LinkTest {

    @Test
    void parseTest() {
        MessageEntity entity = mock(MessageEntity.class);
        when(entity.type()).thenReturn(MessageEntity.Type.bot_command);
        when(entity.length()).thenReturn(6);

        Message message = mock(Message.class);
        when(message.text()).thenReturn("/track link");
        when(message.entities()).thenReturn(new MessageEntity[] {entity});

        Update update = mock(Update.class);
        when(update.message()).thenReturn(message);

        Link link = Link.parse(update);

        assertThat(link.uri()).isEqualTo(URI.create("link"));
    }

    @Test
    void equalsTest() {
        Link first = new Link(URI.create("link"));
        Link second = new Link(URI.create("link"));

        assertThat(first).isEqualTo(second);
    }

    @Test
    void hashCodeTest() {
        Link first = new Link(URI.create("link"));
        Link second = new Link(URI.create("link"));

        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }
}
