package com.github.mtahasahin.evently.mapper;

import com.github.mtahasahin.evently.dto.EventQuestionAnswerDto;
import com.github.mtahasahin.evently.entity.EventQuestionAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper
public interface EventQuestionAnswerMapper {
    @Mapping(source = "question.id", target = "questionId")
    EventQuestionAnswerDto entityToEventQuestionAnswerDto(EventQuestionAnswer eventQuestionAnswers);

    Set<EventQuestionAnswerDto> entityToEventQuestionAnswerDto(Set<EventQuestionAnswer> eventQuestionAnswers);
}

