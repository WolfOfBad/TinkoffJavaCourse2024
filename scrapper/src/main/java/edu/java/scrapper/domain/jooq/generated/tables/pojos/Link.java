/*
 * This file is generated by jOOQ.
 */

package edu.java.scrapper.domain.jooq.generated.tables.pojos;

import jakarta.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.13"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String uri;
    private OffsetDateTime lastUpdate;

    public Link() {
    }

    public Link(Link value) {
        this.id = value.id;
        this.uri = value.uri;
        this.lastUpdate = value.lastUpdate;
    }

    @ConstructorProperties({"id", "uri", "lastUpdate"})
    public Link(
        @Nullable Long id,
        @NotNull String uri,
        @NotNull OffsetDateTime lastUpdate
    ) {
        this.id = id;
        this.uri = uri;
        this.lastUpdate = lastUpdate;
    }

    /**
     * Getter for <code>LINK.ID</code>.
     */
    @Nullable
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>LINK.ID</code>.
     */
    public void setId(@Nullable Long id) {
        this.id = id;
    }

    /**
     * Getter for <code>LINK.URI</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getUri() {
        return this.uri;
    }

    /**
     * Setter for <code>LINK.URI</code>.
     */
    public void setUri(@NotNull String uri) {
        this.uri = uri;
    }

    /**
     * Getter for <code>LINK.LAST_UPDATE</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getLastUpdate() {
        return this.lastUpdate;
    }

    /**
     * Setter for <code>LINK.LAST_UPDATE</code>.
     */
    public void setLastUpdate(@NotNull OffsetDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Link other = (Link) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.uri == null) {
            if (other.uri != null) {
                return false;
            }
        } else if (!this.uri.equals(other.uri)) {
            return false;
        }
        if (this.lastUpdate == null) {
            if (other.lastUpdate != null) {
                return false;
            }
        } else if (!this.lastUpdate.equals(other.lastUpdate)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.uri == null) ? 0 : this.uri.hashCode());
        result = prime * result + ((this.lastUpdate == null) ? 0 : this.lastUpdate.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Link (");

        sb.append(id);
        sb.append(", ").append(uri);
        sb.append(", ").append(lastUpdate);

        sb.append(")");
        return sb.toString();
    }
}