package edu.java.bot.model.link;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class LinkTest {
    @Test
    void equalsTest() {
        Link first = new Link("link");
        Link second = new Link("link");

        assertThat(first).isEqualTo(second);
    }

    @Test
    void hashCodeTest() {
        Link first = new Link("link");
        Link second = new Link("link");

        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }
}
