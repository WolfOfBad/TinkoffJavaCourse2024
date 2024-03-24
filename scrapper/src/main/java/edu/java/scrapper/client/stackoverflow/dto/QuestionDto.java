package edu.java.scrapper.client.stackoverflow.dto;

import edu.java.scrapper.client.Event;
import java.util.List;
import lombok.Builder;

@Builder
public record QuestionDto(
    List<ItemDto> items
) implements Event {

    @Override
    public String getMessage() {
        // maybe some more complex logic in future
        return "новое обновление в вопросе";
    }
}
