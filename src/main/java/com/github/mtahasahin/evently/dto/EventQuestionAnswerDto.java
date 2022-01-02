package com.github.mtahasahin.evently.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventQuestionAnswerDto {
    private UUID questionId;
    private String answer;
}
