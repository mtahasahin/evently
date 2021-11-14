package com.github.mtahasahin.evently.mapper;

import com.github.mtahasahin.evently.dto.CreateUpdateEventForm;
import com.github.mtahasahin.evently.dto.DisplayEventDto;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.entity.Event;
import com.github.mtahasahin.evently.util.RandomStringGenerator;
import com.github.slugify.Slugify;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.Objects;


@Mapper(uses = EventQuestionMapper.class, builder = @Builder(disableBuilder = true))
public abstract class EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventQuestions", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "imagePath", ignore = true)
    @Mapping(target = "key", ignore = true)
    @Mapping(target = "organizer", source = "organizer")
    public abstract void toEvent(CreateUpdateEventForm form, AppUser organizer, @MappingTarget Event entity);

    @BeforeMapping
    public void eventDtoToEvent_BeforeMapping(CreateUpdateEventForm form, @MappingTarget Event event) {
        if(!Objects.equals(event.getName(), form.getName())){
            event.setSlug(new Slugify().slugify(form.getName()) + "-" + RandomStringGenerator.generate(6));
        }
    }

    @AfterMapping
    public void eventDtoToEvent_AfterMapping(CreateUpdateEventForm form, @MappingTarget Event event) {
        if (event.getId() == null) {
            event.setKey(RandomStringGenerator.generate(6));
        }
    }

    @Mapping(target = "waitingApproval", ignore = true)
    @Mapping(target = "organizing", ignore = true)
    @Mapping(target = "joined", ignore = true)
    @Mapping(target = "eventStarted", ignore = true)
    @Mapping(target = "eventEnded", ignore = true)
    @Mapping(source = "entity.eventQuestions", target = "questions")
    public abstract DisplayEventDto eventToDisplayEventDto(Event entity, AppUser user);

    @AfterMapping
    public void eventToDisplayEventDto_AfterMapping(Event entity, AppUser user, @MappingTarget DisplayEventDto dto) {
        if(entity.getAttendeeLimit() == 0){
            dto.setAttendeeLimit(null);
        }
        dto.setOrganizing(user.isOrganizing(entity));
        dto.setWaitingApproval(user.isWaitingForApprovalForEvent(entity));
        dto.setJoined(user.isJoiningEvent(entity));
        dto.setEventStarted(dto.getStartDate().isAfter(LocalDateTime.now()));
        dto.setEventEnded(dto.getEndDate().isAfter(LocalDateTime.now()));
    }
}
