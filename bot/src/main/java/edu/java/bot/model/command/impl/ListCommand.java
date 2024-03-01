package edu.java.bot.model.command.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.User;
import edu.java.bot.model.command.Command;
import edu.java.bot.model.link.Link;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.SendMessageService;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ListCommand implements Command {
    private final UserRepository repository;
    private final SendMessageService sendMessageService;

    @Override
    public void execute(Update update) {
        User user = User.parse(update);

        Optional<List<Link>> links = repository.getLinks(user);

        if (links.isEmpty()) {
            sendMessageService.sendMessage(
                user,
                "Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу с ботом"
            );
        } else if (links.get().isEmpty()) {
            sendMessageService.sendMessage(user, "Сейчас вы не отслеживаете никаких ссылок");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Список отслеживаемых ссылок:\n");

            for (Link link : links.get()) {
                sb.append(link.uri()).append("\n");
            }

            sendMessageService.sendMessage(user, sb.toString());
        }
    }

    @Override
    public String getCommandText() {
        return "/list";
    }

}
