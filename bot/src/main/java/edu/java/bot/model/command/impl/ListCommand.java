package edu.java.bot.model.command.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.model.link.Link;
import edu.java.bot.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ListCommand implements Command {
    private final ObjectProvider<User> userObjectProvider;
    private final UserRepository repository;

    @Override
    public void execute(Update update) {
        User user = userObjectProvider.getObject(update);

        Optional<List<Link>> links = repository.getLinks(user.getId());

        if (links.isEmpty()) {
            user.sendMessage("Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу с ботом");
        } else if (links.get().isEmpty()) {
            user.sendMessage("Сейчас вы не отслеживаете никаких ссылок");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Список отслеживаемых ссылок:\n");

            for (Link link : links.get()) {
                sb.append(link.getUri()).append("\n");
            }

            user.sendMessage(sb.toString());
        }
    }

}
