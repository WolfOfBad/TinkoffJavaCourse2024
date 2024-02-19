package edu.java.bot.model.link;

import java.net.URI;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class LinkTest {
    @Test
    void equalsTest() {
        Link first = new Link(URI.create("link"));
        Link second = new Link(URI.create("link"));

        assertThat(first).isEqualTo(second);
    }

    @Test
    void hashCodeTest() {
        Link first = new Link(URI.create("link"));
        Link second = new Link(URI.create("link"));

        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }
}
