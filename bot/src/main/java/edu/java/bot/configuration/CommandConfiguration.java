package edu.java.bot.configuration;

import edu.java.bot.model.User;
import edu.java.bot.model.command.impl.FailCommand;
import edu.java.bot.model.command.impl.HelpCommand;
import edu.java.bot.model.command.impl.ListCommand;
import edu.java.bot.model.command.impl.ResetCommand;
import edu.java.bot.model.command.impl.StartCommand;
import edu.java.bot.model.command.impl.TrackCommand;
import edu.java.bot.model.command.impl.UntrackCommand;
import edu.java.bot.model.link.Link;
import edu.java.bot.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootConfiguration
@AllArgsConstructor
public class CommandConfiguration {
    private final ApplicationContext context;

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public FailCommand failCommand(User user, String message) {
        return new FailCommand(user, message);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public StartCommand startCommand(User user) {
        return new StartCommand(user, context.getBean(UserRepository.class));
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ResetCommand resetCommand(User user) {
        return new ResetCommand(user, context.getBean(UserRepository.class));
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public HelpCommand helpCommand(User user) {
        return new HelpCommand(user);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public TrackCommand trackCommand(User user, Link link) {
        return new TrackCommand(user, context.getBean(UserRepository.class), link);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public UntrackCommand untrackCommand(User user, Link link) {
        return new UntrackCommand(user, context.getBean(UserRepository.class), link);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ListCommand listCommand(User user) {
        return new ListCommand(user, context.getBean(UserRepository.class));
    }

}
