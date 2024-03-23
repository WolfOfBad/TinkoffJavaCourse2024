package edu.java.scrapper.client.github.dto.event;

public enum Action {
    OPENED,
    CLOSED,
    UNKNOWN;

    public String getActionMessage() {
        return switch (this) {
            case OPENED -> "новый ПР в репозитории";
            case CLOSED -> "один из ПР в репозитории закрыт";
            case UNKNOWN -> "замечено обновление в ПР";
        };
    }

}
