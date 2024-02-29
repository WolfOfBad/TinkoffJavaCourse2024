package edu.java.bot.model.link;

import java.net.URI;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
@Getter
public class Link {
    private final URI uri;

    public Link(String uri) {
        this.uri = URI.create(uri);
    }

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
