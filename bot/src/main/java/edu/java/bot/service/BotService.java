package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.configuration.ApplicationConfigProperties;
import edu.java.bot.model.command.Command;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BotService implements UpdatesListener, AutoCloseable {
    private final CommandParser parser;

    @Getter
    private final TelegramBot bot;

    public BotService(ApplicationConfigProperties configProperties, CommandParser parser) {
        bot = new TelegramBot(configProperties.telegramToken());
        this.parser = parser;


        setCommandsMenu();

        bot.setUpdatesListener(this);
    }

    private void setCommandsMenu() {
        BotCommand startCommand = new BotCommand("start", "Начать работу с ботом");
        BotCommand helpCommand = new BotCommand("help", "Список доступных команд");
        BotCommand trackCommand = new BotCommand("track", "Начать отслеживание ссылки");
        BotCommand untrackCommand = new BotCommand("untrack", "Прекратить отслеживание ссылки");
        BotCommand listCommand = new BotCommand("list", "Список отслеживаемых ссылок");
        BotCommand resetCommand = new BotCommand("reset", "Сбросить все отслеживаемые ссылки");

        SetMyCommands request = new SetMyCommands(
            startCommand,
            helpCommand,
            trackCommand,
            untrackCommand,
            listCommand,
            resetCommand
        );

        BaseResponse response = bot.execute(request);

        if (response.isOk()) {
            log.info("Commands menu successfully set");
        } else {
            log.info("Commands menu not set:" + response.errorCode());
        }
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
