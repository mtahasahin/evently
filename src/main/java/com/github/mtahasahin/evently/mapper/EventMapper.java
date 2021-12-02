package com.github.mtahasahin.evently.mapper;

import com.github.mtahasahin.evently.dto.CreateUpdateEventForm;
import com.github.mtahasahin.evently.dto.DisplayEventDto;
import com.github.mtahasahin.evently.dto.EventQuestionDto;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.entity.Event;
import com.github.mtahasahin.evently.enums.EventLocationType;
import com.github.mtahasahin.evently.util.RandomStringGenerator;
import com.github.slugify.Slugify;
import org.mapstruct.*;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Objects;


@Mapper(uses = {EventQuestionMapper.class, UserMapper.class}, builder = @Builder(disableBuilder = true))
public abstract class EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventQuestions", ignore = true)
    @Mapping(target = "eventApplications", ignore = true)
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
        if(form.getEventLocationType() == EventLocationType.ONLINE){
            event.setLocation(null);
        }
        else if(form.getEventLocationType() == EventLocationType.IN_PERSON){
            event.setEventUrl(null);
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
        dto.setEventStarted(ZonedDateTime.of(dto.getStartDate(), ZoneId.of(dto.getTimezone())).toLocalDateTime().isBefore(LocalDateTime.now()));
        dto.setEventEnded(ZonedDateTime.of(dto.getEndDate(), ZoneId.of(dto.getTimezone())).toLocalDateTime().isBefore(LocalDateTime.now()));

        dto.getQuestions().sort(Comparator.comparing(EventQuestionDto::getOrder));

        if(!dto.isCanSee()) {
            dto.setEventUrl(null);
        }

        PolicyFactory policy = new HtmlPolicyBuilder()
                .allowElements("h2","h3","h4","ul","ol","li","p","strong","i","a")
                .allowAttributes("href").onElements("a")
                .allowStandardUrlProtocols()
                .toFactory();

        dto.setDescription(policy.sanitize(dto.getDescription()));
    }
}
