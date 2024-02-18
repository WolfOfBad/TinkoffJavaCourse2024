package edu.java.bot.configuration;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.service.BotService;
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
    public Link link(Update update) {
        return Link.parse(update);
    }

}
