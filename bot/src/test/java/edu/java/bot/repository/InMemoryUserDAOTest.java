
package edu.java.bot.repository;

import edu.java.bot.model.User;
import edu.java.bot.model.link.Link;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class InMemoryUserDAOTest {

    @Test
    void registerTest() {
        User user = new User(1);
        UserRepository repository = new InMemoryUserDAO();

        UserRepository.Result result = repository.register(user);

        assertThat(result).isEqualTo(UserRepository.Result.OK);
    }

    @Test
    void multipleRegisterTest() {
        User user = new User(1);
        UserRepository repository = new InMemoryUserDAO();

        repository.register(user);
        UserRepository.Result result = repository.register(user);

        assertThat(result).isEqualTo(UserRepository.Result.USER_ALREADY_EXIST);
    }

    @Test
    void addLinkTest() {
        Link link = new Link(URI.create("link"));
        User user = new User(1);

        UserRepository repository = new InMemoryUserDAO();
        repository.register(user);

        UserRepository.Result result = repository.addLink(user, link);

        assertThat(result).isEqualTo(UserRepository.Result.OK);
    }

    @Test
    void multipleAddLinkTest() {
        Link link = new Link(URI.create("link"));
        User user = new User(1);

        UserRepository repository = new InMemoryUserDAO();
        repository.register(user);

        repository.addLink(user, link);
        UserRepository.Result result = repository.addLink(user, link);

        assertThat(result).isEqualTo(UserRepository.Result.LINK_ALREADY_EXIST);
    }

    @Test
    void userNotExistAddLinkTest() {
        Link link = new Link(URI.create("link"));
        User user = new User(1);

        UserRepository repository = new InMemoryUserDAO();

        UserRepository.Result result = repository.addLink(user, link);

        assertThat(result).isEqualTo(UserRepository.Result.USER_NOT_EXIST);
    }

    @Test
    void deleteLinkTest() {
        Link link = new Link(URI.create("link"));
        User user = new User(1);

        UserRepository repository = new InMemoryUserDAO();
        repository.register(user);
        repository.addLink(user, link);

        UserRepository.Result result = repository.deleteLink(user, link);

        assertThat(result).isEqualTo(UserRepository.Result.OK);

    }

    @Test
    void linkNotExistDeleteLinkTest() {
        Link link = new Link(URI.create("link"));
        User user = new User(1);

        UserRepository repository = new InMemoryUserDAO();
        repository.register(user);

        UserRepository.Result result = repository.deleteLink(user, link);

        assertThat(result).isEqualTo(UserRepository.Result.LINK_NOT_EXIST);
    }

    @Test
    void userNotExistDeleteLinkTest() {
        Link link = new Link(URI.create("link"));
        User user = new User(1);

        UserRepository repository = new InMemoryUserDAO();

        UserRepository.Result result = repository.deleteLink(user, link);

        assertThat(result).isEqualTo(UserRepository.Result.USER_NOT_EXIST);
    }

    @Test
    void deleteUserTest() {
        User user = new User(1);

        UserRepository repository = new InMemoryUserDAO();
        repository.register(user);

        UserRepository.Result result = repository.deleteUser(user);

        assertThat(result).isEqualTo(UserRepository.Result.OK);
    }

    @Test
    void userNotExistDeleteUserTest() {
        User user = new User(1);

        UserRepository repository = new InMemoryUserDAO();

        UserRepository.Result result = repository.deleteUser(user);

        assertThat(result).isEqualTo(UserRepository.Result.USER_NOT_EXIST);
    }

    @Test
    void getLinksTest() {
        User user = new User(1);

        UserRepository repository = new InMemoryUserDAO();
        repository.register(user);

        Optional<List<Link>> result = repository.getLinks(user);

        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    void userNotExistGetLinksTest() {
        User user = new User(1);

        UserRepository repository = new InMemoryUserDAO();

        Optional<List<Link>> result = repository.getLinks(user);

        assertThat(result.isEmpty()).isTrue();
    }
}
