package edu.java.bot.model;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTest {
    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private com.pengrad.telegrambot.model.User user;

    @BeforeEach
    public void before() {
        when(update.message()).thenReturn(message);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(1L);
    }

    @Test
    void parseTest() {
        User user = User.parse(update);

        assertThat(user.id()).isEqualTo(1);
    }
}
