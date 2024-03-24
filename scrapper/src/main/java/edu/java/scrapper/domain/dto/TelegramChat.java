package edu.java.scrapper.domain.dto;

import lombok.Builder;

@Builder
public record TelegramChat(
    long id,
    long tgChatId
) {
}
