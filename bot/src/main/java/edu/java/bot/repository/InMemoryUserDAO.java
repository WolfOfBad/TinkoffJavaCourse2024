package edu.java.bot.repository;

import edu.java.bot.model.Link;
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
    public Result register(long id) {
        if (storage.containsKey(id)) {
            return Result.USER_ALREADY_EXIST;
        }

        storage.put(id, new ArrayList<>());
        return Result.OK;
    }

    @Override
    public Result addLink(long id, Link link) {
        if (!storage.containsKey(id)) {
            return Result.USER_NOT_EXIST;
        }

        if (storage.get(id).contains(link)) {
            return Result.LINK_ALREADY_EXIST;
        }

        storage.get(id).add(link);
        return Result.OK;
    }

    @Override
    public Result deleteLink(long id, Link link) {
        if (!storage.containsKey(id)) {
            return Result.USER_NOT_EXIST;
        }

        if (!storage.get(id).contains(link)) {
            return Result.LINK_NOT_EXIST;
        }

        storage.get(id).remove(link);
        return Result.OK;
    }

    @Override
    public Result deleteUser(long id) {
        if (!storage.containsKey(id)) {
            return Result.USER_NOT_EXIST;
        }

        storage.remove(id);
        return Result.OK;
    }

    @Override
    public Optional<List<Link>> getLinks(long id) {
        if (!storage.containsKey(id)) {
            return Optional.empty();
        }

        return Optional.of(storage.get(id));
    }

}
