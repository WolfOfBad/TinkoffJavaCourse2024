package edu.java.bot.repository;

import edu.java.bot.model.User;
import edu.java.bot.model.link.Link;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Result register(User user);

    Result addLink(User user, Link link);

    Result deleteLink(User user, Link link);

    Result deleteUser(User user);

    Optional<List<Link>> getLinks(User user);

    enum Result {
        OK,
        USER_ALREADY_EXIST,
        USER_NOT_EXIST,
        LINK_ALREADY_EXIST,
        LINK_NOT_EXIST
    }
}
