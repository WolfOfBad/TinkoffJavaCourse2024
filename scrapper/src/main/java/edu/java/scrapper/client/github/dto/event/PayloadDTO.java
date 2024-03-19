package edu.java.scrapper.client.github.dto.event;

import lombok.Builder;

@Builder
public record PayloadDTO(
    String action
) {
    public Action getAction() {
        return switch (action) {
            case "opened" -> Action.OPENED;
            case "closed" -> Action.CLOSED;
            default -> Action.UNKNOWN;
        };
    }
}
