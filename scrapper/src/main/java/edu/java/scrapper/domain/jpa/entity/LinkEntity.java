package edu.java.scrapper.domain.jpa.entity;

import edu.java.scrapper.domain.jpa.UriPersistenceConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "link", indexes = {@Index(name = "uri_index", columnList = "uri")})
@Getter
@Setter
public class LinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uri", unique = true)
    @NotNull
    @Convert(converter = UriPersistenceConverter.class)
    private URI uri;

    @Column(name = "last_update")
    @NotNull
    private OffsetDateTime lastUpdate;

    @ManyToMany(mappedBy = "links")
    private List<ChatEntity> chats = new ArrayList<>();

}
