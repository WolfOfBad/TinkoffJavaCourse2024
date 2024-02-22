package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.model.command.Command;
import edu.java.bot.model.command.impl.HelpCommand;
import edu.java.bot.model.command.impl.ListCommand;
import edu.java.bot.model.command.impl.ResetCommand;
import edu.java.bot.model.command.impl.StartCommand;
import edu.java.bot.model.command.impl.TrackCommand;
import edu.java.bot.model.command.impl.UntrackCommand;
import edu.java.bot.model.link.parser.GithubParser;
import edu.java.bot.model.link.parser.LinkParser;
import edu.java.bot.model.link.parser.StackOverflowParser;
import edu.java.bot.service.BotService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ApplicationConfiguration {
    @Bean
    @Primary
    public LinkParser parserChain(ApplicationContext context) {
        return LinkParser.link(
            context.getBean(GithubParser.class),
            context.getBean(StackOverflowParser.class)
        );
    }

    @Bean
    public Map<String, Command> commandMap(ApplicationContext context) {
        Map<String, Command> commandMap = new HashMap<>();
        commandMap.put("/start", context.getBean(StartCommand.class));
        commandMap.put("/reset", context.getBean(ResetCommand.class));
        commandMap.put("/help", context.getBean(HelpCommand.class));
        commandMap.put("/list", context.getBean(ListCommand.class));
        commandMap.put("/track", context.getBean(TrackCommand.class));
        commandMap.put("/untrack", context.getBean(UntrackCommand.class));

        return commandMap;
    }

    @Bean
    public TelegramBot telegramBot(BotService service) {
        return service.getBot();
    }

}
