package edu.java.client.bot.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(
    @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("id")
    long id,
    @Valid
    @Schema(name = "url", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("url")
    URI uri,
    @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("description")
    String description,
    @Valid
    @Schema(name = "tgChatIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("tgChatIds")
    List<Long> telegramChatId
) {

}
