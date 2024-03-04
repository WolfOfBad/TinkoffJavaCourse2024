package edu.java.scrapper.client.stackoverflow.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record QuestionDto(
    List<ItemDto> items
) {

}
