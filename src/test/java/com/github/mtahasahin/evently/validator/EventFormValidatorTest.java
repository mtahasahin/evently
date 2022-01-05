package com.github.mtahasahin.evently.validator;

import com.github.mtahasahin.evently.dto.CreateUpdateEventForm;
import com.github.mtahasahin.evently.enums.EventLocationType;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventFormValidatorTest {

    @Test
    void validate_returnErrorWhenLocationNullForInPersonEvent() {
        EventFormValidator eventFormValidator = new EventFormValidator();
        var eventForm = new CreateUpdateEventForm();
        eventForm.setEventLocationType(EventLocationType.IN_PERSON);
        eventForm.setLocation(null);
        eventForm.setLimited(false);
        eventForm.setAttendeeLimit(0);
        eventForm.setTimezone(ZoneId.systemDefault().getId());
        eventForm.setStartDate(LocalDateTime.now().plusDays(1));
        eventForm.setEndDate(LocalDateTime.now().plusDays(2));
        Errors errors = new BeanPropertyBindingResult(eventForm, "eventForm");
        eventFormValidator.validate(eventForm, errors);
        assertEquals(1, errors.getAllErrors().size());
        assertEquals(1, errors.getFieldErrorCount("location"));
    }

    @Test
    void validate_returnErrorWhenURLIsNullForOnlineEvent() {
        EventFormValidator eventFormValidator = new EventFormValidator();
        var eventForm = new CreateUpdateEventForm();
        eventForm.setEventLocationType(EventLocationType.ONLINE);
        eventForm.setEventUrl(null);
        eventForm.setLimited(false);
        eventForm.setAttendeeLimit(0);
        eventForm.setTimezone(ZoneId.systemDefault().getId());
        eventForm.setStartDate(LocalDateTime.now().plusDays(1));
        eventForm.setEndDate(LocalDateTime.now().plusDays(2));
        Errors errors = new BeanPropertyBindingResult(eventForm, "eventForm");
        eventFormValidator.validate(eventForm, errors);
        assertEquals(1, errors.getAllErrors().size());
        assertEquals(1, errors.getFieldErrorCount("eventUrl"));
    }

    @Test
    void validate_returnErrorWhenEventStartDateIsInPast() {
        EventFormValidator eventFormValidator = new EventFormValidator();
        var eventForm = new CreateUpdateEventForm();
        eventForm.setEventLocationType(EventLocationType.ONLINE);
        eventForm.setEventUrl("https://www.example.com");
        eventForm.setLimited(false);
        eventForm.setAttendeeLimit(0);
        eventForm.setTimezone(ZoneId.systemDefault().getId());
        eventForm.setStartDate(LocalDateTime.now().minusHours(1));
        eventForm.setEndDate(LocalDateTime.now().plusDays(2));
        Errors errors = new BeanPropertyBindingResult(eventForm, "eventForm");
        eventFormValidator.validate(eventForm, errors);
        assertEquals(1, errors.getAllErrors().size());
        assertEquals(1, errors.getFieldErrorCount("startDate"));
    }

    @Test
    void validate_returnErrorWhenEventEndDateIsBeforeStartDate() {
        EventFormValidator eventFormValidator = new EventFormValidator();
        var eventForm = new CreateUpdateEventForm();
        eventForm.setEventLocationType(EventLocationType.ONLINE);
        eventForm.setEventUrl("https://www.example.com");
        eventForm.setLimited(false);
        eventForm.setAttendeeLimit(0);
        eventForm.setTimezone(ZoneId.systemDefault().getId());
        eventForm.setStartDate(LocalDateTime.now().plusDays(2));
        eventForm.setEndDate(LocalDateTime.now().plusDays(1));
        Errors errors = new BeanPropertyBindingResult(eventForm, "eventForm");
        eventFormValidator.validate(eventForm, errors);
        assertEquals(1, errors.getAllErrors().size());
        assertEquals(1, errors.getFieldErrorCount("endDate"));
    }

    @Test
    void validate_returnErrorWhenEventIsLimitedAndAttendeeLimitIsSmallerThan2() {
        EventFormValidator eventFormValidator = new EventFormValidator();
        var eventForm = new CreateUpdateEventForm();
        eventForm.setEventLocationType(EventLocationType.ONLINE);
        eventForm.setEventUrl("https://www.example.com");
        eventForm.setLimited(true);
        eventForm.setAttendeeLimit(1);
        eventForm.setTimezone(ZoneId.systemDefault().getId());
        eventForm.setStartDate(LocalDateTime.now().plusDays(1));
        eventForm.setEndDate(LocalDateTime.now().plusDays(2));
        Errors errors = new BeanPropertyBindingResult(eventForm, "eventForm");
        eventFormValidator.validate(eventForm, errors);
        assertEquals(1, errors.getAllErrors().size());
        assertEquals(1, errors.getFieldErrorCount("attendeeLimit"));
    }

    @Test
    void validate_returnErrorWhenImageIsInvalid() {
        EventFormValidator eventFormValidator = new EventFormValidator();
        var eventForm = new CreateUpdateEventForm();
        eventForm.setEventLocationType(EventLocationType.ONLINE);
        eventForm.setEventUrl("https://www.example.com");
        eventForm.setLimited(false);
        eventForm.setAttendeeLimit(0);
        eventForm.setTimezone(ZoneId.systemDefault().getId());
        eventForm.setStartDate(LocalDateTime.now().plusDays(1));
        eventForm.setEndDate(LocalDateTime.now().plusDays(2));
        eventForm.setImage(new MockMultipartFile("file", "file.jpg", "image/jpeg", "some xml".getBytes()));
        Errors errors = new BeanPropertyBindingResult(eventForm, "eventForm");
        eventFormValidator.validate(eventForm, errors);
        assertEquals(1, errors.getAllErrors().size());
        assertEquals(1, errors.getFieldErrorCount("image"));
    }


}