package edu.java.scrapper.client.github.dto;

import lombok.Builder;

@Builder
public record UserDto(
    long id,
    String login
) {

}
