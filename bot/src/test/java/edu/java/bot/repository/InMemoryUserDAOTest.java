package edu.java.bot.repository;

import edu.java.bot.model.Link;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class InMemoryUserDAOTest {

    @Test
    void registerTest() {
        long id = 1;
        UserRepository repository = new InMemoryUserDAO();

        UserRepository.Result result = repository.register(id);

        assertThat(result).isEqualTo(UserRepository.Result.OK);
    }

    @Test
    void multipleRegisterTest() {
        long id = 1;
        UserRepository repository = new InMemoryUserDAO();

        repository.register(id);
        UserRepository.Result result = repository.register(id);

        assertThat(result).isEqualTo(UserRepository.Result.USER_ALREADY_EXIST);
    }

    @Test
    void addLinkTest() {
        Link link = new Link(URI.create("link"));
        long id = 1;

        UserRepository repository = new InMemoryUserDAO();
        repository.register(id);

        UserRepository.Result result = repository.addLink(id, link);

        assertThat(result).isEqualTo(UserRepository.Result.OK);
    }

    @Test
    void multipleAddLinkTest() {
        Link link = new Link(URI.create("link"));
        long id = 1;

        UserRepository repository = new InMemoryUserDAO();
        repository.register(id);

        repository.addLink(id, link);
        UserRepository.Result result = repository.addLink(id, link);

        assertThat(result).isEqualTo(UserRepository.Result.LINK_ALREADY_EXIST);
    }

    @Test
    void userNotExistAddLinkTest() {
        Link link = new Link(URI.create("link"));
        long id = 1;

        UserRepository repository = new InMemoryUserDAO();

        UserRepository.Result result = repository.addLink(id, link);

        assertThat(result).isEqualTo(UserRepository.Result.USER_NOT_EXIST);
    }

    @Test
    void deleteLinkTest() {
        Link link = new Link(URI.create("link"));
        long id = 1;

        UserRepository repository = new InMemoryUserDAO();
        repository.register(id);
        repository.addLink(id, link);

        UserRepository.Result result = repository.deleteLink(id, link);

        assertThat(result).isEqualTo(UserRepository.Result.OK);

    }

    @Test
    void linkNotExistDeleteLinkTest() {
        Link link = new Link(URI.create("link"));
        long id = 1;

        UserRepository repository = new InMemoryUserDAO();
        repository.register(id);

        UserRepository.Result result = repository.deleteLink(id, link);

        assertThat(result).isEqualTo(UserRepository.Result.LINK_NOT_EXIST);
    }

    @Test
    void userNotExistDeleteLinkTest() {
        Link link = new Link(URI.create("link"));
        long id = 1;

        UserRepository repository = new InMemoryUserDAO();

        UserRepository.Result result = repository.deleteLink(id, link);

        assertThat(result).isEqualTo(UserRepository.Result.USER_NOT_EXIST);
    }

    @Test
    void deleteUserTest() {
        long id = 1;

        UserRepository repository = new InMemoryUserDAO();
        repository.register(id);

        UserRepository.Result result = repository.deleteUser(id);

        assertThat(result).isEqualTo(UserRepository.Result.OK);
    }

    @Test
    void userNotExistDeleteUserTest() {
        long id = 1;

        UserRepository repository = new InMemoryUserDAO();

        UserRepository.Result result = repository.deleteUser(id);

        assertThat(result).isEqualTo(UserRepository.Result.USER_NOT_EXIST);
    }

    @Test
    void getLinksTest() {
        long id = 1;

        UserRepository repository = new InMemoryUserDAO();
        repository.register(id);

        Optional<List<Link>> result = repository.getLinks(id);

        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    void userNotExistGetLinksTest() {
        long id = 1;

        UserRepository repository = new InMemoryUserDAO();

        Optional<List<Link>> result = repository.getLinks(id);

        assertThat(result.isEmpty()).isTrue();
    }
}
