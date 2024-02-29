package edu.java.bot.repository;

import edu.java.bot.model.User;
import edu.java.bot.model.link.Link;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class InMemoryUserDAO implements UserRepository {
    private final Map<Long, List<Link>> storage = new HashMap<>();

    @Override
    public Result register(User user) {
        if (storage.containsKey(user.id())) {
            return Result.USER_ALREADY_EXIST;
        }

        storage.put(user.id(), new ArrayList<>());
        return Result.OK;
    }

    @Override
    public Result addLink(User user, Link link) {
        if (!storage.containsKey(user.id())) {
            return Result.USER_NOT_EXIST;
        }

        if (storage.get(user.id()).contains(link)) {
            return Result.LINK_ALREADY_EXIST;
        }

        storage.get(user.id()).add(link);
        return Result.OK;
    }

    @Override
    public Result deleteLink(User user, Link link) {
        if (!storage.containsKey(user.id())) {
            return Result.USER_NOT_EXIST;
        }

        if (!storage.get(user.id()).contains(link)) {
            return Result.LINK_NOT_EXIST;
        }

        storage.get(user.id()).remove(link);
        return Result.OK;
    }

    @Override
    public Result deleteUser(User user) {
        if (!storage.containsKey(user.id())) {
            return Result.USER_NOT_EXIST;
        }

        storage.remove(user.id());
        return Result.OK;
    }

    @Override
    public Optional<List<Link>> getLinks(User user) {
        if (!storage.containsKey(user.id())) {
            return Optional.empty();
        }

        return Optional.of(storage.get(user.id()));
    }

}
