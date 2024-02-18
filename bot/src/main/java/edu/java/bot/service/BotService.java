package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.configuration.ApplicationConfigProperties;
import edu.java.bot.model.command.Command;
import java.util.List;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class BotService implements UpdatesListener, AutoCloseable {
    private final CommandParser parser;

    @Getter
    private final TelegramBot bot;

    public BotService(ApplicationConfigProperties configProperties, CommandParser parser) {
        bot = new TelegramBot(configProperties.telegramToken());
        this.parser = parser;

        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            Command command = parser.parse(update);
            command.execute();
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void close() {
        bot.shutdown();
    }
}
