package edu.java.scrapper.domain.dto;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record Link(
    long id,
    URI uri,
    OffsetDateTime lastUpdate
) {
}
