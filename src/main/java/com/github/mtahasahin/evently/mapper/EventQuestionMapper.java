package com.github.mtahasahin.evently.mapper;

import com.github.mtahasahin.evently.dto.EventQuestionDto;
import com.github.mtahasahin.evently.entity.EventQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@Mapper
public interface EventQuestionMapper {

    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "event", ignore = true)
    void eventQuestionDtoToEntity(EventQuestionDto dto, @MappingTarget EventQuestion entity);

    EventQuestionDto eventQuestionEntityToDto(EventQuestion entity);

    List<EventQuestionDto> eventQuestionEntityToDto(Set<EventQuestion> entities);
}
