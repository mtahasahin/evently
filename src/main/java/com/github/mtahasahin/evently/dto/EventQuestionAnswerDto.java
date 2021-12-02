package com.github.mtahasahin.evently.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventQuestionAnswerDto {
    private Long questionId;
    private String answer;
}
