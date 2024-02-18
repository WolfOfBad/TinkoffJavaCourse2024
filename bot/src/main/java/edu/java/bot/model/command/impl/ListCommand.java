package edu.java.bot.model.command.impl;

import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ListCommand implements Command {
    private final User user;
    private final UserRepository repository;

    @Override
    public void execute() {
        Optional<List<Link>> links = repository.getLinks(user.id());

        if (links.isEmpty()) {
            user.sendMessage("Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу с ботом");
        } else if (links.get().isEmpty()) {
            user.sendMessage("Сейчас вы не отслеживаете никаких ссылок");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Список отслеживаемых ссылок:\n");

            for (Link link : links.get()) {
                sb.append(link.uri()).append("\n");
            }

            user.sendMessage(sb.toString());
        }

    }
}
