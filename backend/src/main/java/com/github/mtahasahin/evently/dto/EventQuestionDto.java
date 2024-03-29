package com.github.mtahasahin.evently.dto;

import com.github.mtahasahin.evently.enums.EventQuestionType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;


@Builder
@Data
public class EventQuestionDto {
    private UUID id;
    @NotNull
    private EventQuestionType type;
    private int order;
    @NotBlank
    private String title;
    private String description;
    private boolean required;
}
