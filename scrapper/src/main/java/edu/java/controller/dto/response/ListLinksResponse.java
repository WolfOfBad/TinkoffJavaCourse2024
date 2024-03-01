package edu.java.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;

public record ListLinksResponse(
    @Valid
    @Schema(name = "links", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("links")
    List<@Valid LinkResponse> links,
    @Schema(name = "size", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("size")
    int size
) {
}
