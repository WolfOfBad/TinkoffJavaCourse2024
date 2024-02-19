package edu.java.bot.configuration;

import edu.java.bot.model.User;
import edu.java.bot.model.link.Link;
import edu.java.bot.model.link.parser.GithubParser;
import edu.java.bot.model.link.parser.LinkParser;
import edu.java.bot.model.link.parser.StackOverflowParser;
import edu.java.bot.service.BotService;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootConfiguration
@AllArgsConstructor
public class ApplicationConfiguration {
    private final ApplicationContext context;

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public User user(long id) {
        return new User(
            id,
            context.getBean(BotService.class).getBot()
        );
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Link link(URI uri) {
        return new Link(uri);
    }

    @Bean
    public LinkParser parserChain() {
        return LinkParser.link(
            new GithubParser(context),
            new StackOverflowParser(context)
        );
    }

}
