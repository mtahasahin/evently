package com.github.mtahasahin.evently.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class EventApplicationDto {
    private Long id;
    private Set<EventQuestionAnswerDto> answers;
    @JsonProperty("has_approved")
    private boolean approved;
    private List<EventQuestionDto> questions;
    private UserLightDto user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
