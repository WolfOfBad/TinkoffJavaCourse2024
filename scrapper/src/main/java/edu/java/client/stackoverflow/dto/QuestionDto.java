package edu.java.client.stackoverflow.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionDto {
    List<ItemDto> items;
}
