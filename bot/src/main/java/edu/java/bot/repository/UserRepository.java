package edu.java.bot.repository;

import edu.java.bot.model.link.Link;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Result register(long id);

    Result addLink(long id, Link link);

    Result deleteLink(long id, Link link);

    Result deleteUser(long id);

    Optional<List<Link>> getLinks(long id);

    enum Result {
        OK,
        USER_ALREADY_EXIST,
        USER_NOT_EXIST,
        LINK_ALREADY_EXIST,
        LINK_NOT_EXIST
    }
}
