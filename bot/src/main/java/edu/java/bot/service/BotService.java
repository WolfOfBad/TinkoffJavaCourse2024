package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.model.command.Command;
import io.micrometer.core.instrument.Counter;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class BotService implements UpdatesListener, AutoCloseable {
    private final CommandParser parser;
    private final Logger logger = LogManager.getLogger();
    private final TelegramBot bot;
    private final Counter counter;

    public BotService(TelegramBot bot, CommandParser parser, Counter counter) {
        this.bot = bot;
        this.parser = parser;
        this.counter = counter;

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
            logger.info("Commands menu successfully set");
        } else {
            logger.info("Commands menu not set:" + response.errorCode());
        }
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            // skip edited messages
            if (update.message() == null) {
                continue;
            }

            Command command = parser.parse(update);
            command.execute(update);

            counter.increment();
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void close() {
        bot.shutdown();
    }
}
