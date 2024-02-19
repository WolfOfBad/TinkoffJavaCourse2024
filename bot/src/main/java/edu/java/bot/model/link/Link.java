package edu.java.bot.model.link;

import java.net.URI;

public record Link(
    URI uri
) {
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Link)) {
            return false;
        }
        return uri.equals(((Link) object).uri);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }

}
