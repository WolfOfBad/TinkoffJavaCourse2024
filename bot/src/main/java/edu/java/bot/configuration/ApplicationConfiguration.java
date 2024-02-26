package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.model.command.Command;
import edu.java.bot.model.link.parser.GithubParser;
import edu.java.bot.model.link.parser.LinkParserManager;
import edu.java.bot.model.link.parser.StackOverflowParser;
import edu.java.bot.service.BotService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ApplicationConfiguration {
    @Bean
    @Primary
    public LinkParserManager parserChain(
        GithubParser githubParser,
        StackOverflowParser stackOverflowParser
    ) {
        return LinkParserManager.link(
            githubParser,
            stackOverflowParser
        );
    }

    @Bean
    public Map<String, Command> commandMap(List<Command> commands) {
        Map<String, Command> commandMap = new HashMap<>();
        for (Command command : commands) {
            if (command.getCommandText() != null) {
                commandMap.put(command.getCommandText(), command);
            }
        }

        return commandMap;
    }

    @Bean
    public TelegramBot telegramBot(BotService service) {
        return service.getBot();
    }

}
