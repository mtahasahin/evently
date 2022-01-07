package com.github.mtahasahin.evently.mapper;

import com.github.mtahasahin.evently.dto.CreateUpdateEventForm;
import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.entity.Event;
import com.github.mtahasahin.evently.enums.EventLocationType;
import com.github.mtahasahin.evently.enums.EventVisibility;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RequiredArgsConstructor
class EventMapperTest {
    private EventMapper eventMapper;

    @BeforeEach
    void before() {
        eventMapper = new EventMapperImpl(new EventQuestionMapperImpl(), new UserMapperImpl(new ProfileMapperImpl()));
    }

    @Test
    void eventDtoToEvent() {
        AppUser user = AppUser.builder()
                .username("user")
                .build();

        var eventDto = new CreateUpdateEventForm();
        eventDto.setName("event");
        eventDto.setEventLocationType(EventLocationType.ONLINE);
        eventDto.setEventUrl("https://www.example.com");
        eventDto.setLimited(false);
        eventDto.setAttendeeLimit(0);
        eventDto.setTimezone(ZoneId.systemDefault().getId());
        eventDto.setStartDate(LocalDateTime.now().minusHours(1));
        eventDto.setEndDate(LocalDateTime.now().plusDays(2));
        eventDto.setApprovalRequired(false);
        eventDto.setDescription("description");
        eventDto.setLanguage("en");
        eventDto.setVisibility(EventVisibility.PUBLIC);

        var eventEntity = new Event();

        eventMapper.toEvent(eventDto, user, eventEntity);

        assertEquals(eventDto.getEventUrl(), eventEntity.getEventUrl());
        assertEquals(eventDto.getDescription(), eventEntity.getDescription());
        assertEquals(eventDto.getEventLocationType(), eventEntity.getEventLocationType());
        assertEquals(eventDto.getEndDate(), eventEntity.getEndDate());
        assertEquals(eventDto.getStartDate(), eventEntity.getStartDate());
        assertEquals(eventDto.getName(), eventEntity.getName());
        assertEquals(eventDto.getLanguage(), eventEntity.getLanguage());
        assertEquals(eventDto.getTimezone(), eventEntity.getTimezone());
        assertEquals(0, eventEntity.getAttendeeLimit());
        assertFalse(eventEntity.isLimited());
        assertFalse(eventEntity.isApprovalRequired());
        assertEquals(eventDto.getVisibility(), eventEntity.getVisibility());
        assertEquals(user.getUsername(), eventEntity.getOrganizer().getUsername());

    }
}