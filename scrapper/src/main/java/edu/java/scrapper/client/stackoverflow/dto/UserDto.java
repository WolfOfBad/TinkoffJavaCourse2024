package edu.java.scrapper.client.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UserDto(
    @JsonProperty("account_id")
    long id,
    @JsonProperty("display_name")
    String name
) {

}
