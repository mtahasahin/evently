package com.github.mtahasahin.evently.validator;

import com.github.mtahasahin.evently.dto.CreateUpdateEventForm;
import com.github.mtahasahin.evently.enums.EventLocationType;
import com.github.mtahasahin.evently.util.ImageUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.OffsetDateTime;
import java.time.ZoneId;


@Component
public class EventFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateUpdateEventForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateUpdateEventForm eventForm = (CreateUpdateEventForm) target;
        if (eventForm.getEventLocationType() == EventLocationType.ONLINE && Strings.isBlank(eventForm.getEventUrl())) {
            errors.rejectValue("eventUrl", null, "can not be empty");
        } else if (eventForm.getEventLocationType() == EventLocationType.IN_PERSON && Strings.isBlank(eventForm.getLocation())) {
            errors.rejectValue("location", null, "can not be empty");
        }

        if(eventForm.getLimited() && eventForm.getAttendeeLimit() < 2){
            errors.rejectValue("attendeeLimit", null, "can not be less than 2");
        }

        if (eventForm.getStartDate().isAfter(eventForm.getEndDate())) {
            errors.rejectValue("endDate", null, "can not be before start date");
        }

        ZoneId zoneId = ZoneId.of(eventForm.getTimezone());
        OffsetDateTime startDate = eventForm.getStartDate().atZone(zoneId).toOffsetDateTime();
        if (startDate.isBefore(OffsetDateTime.now())) {
            errors.rejectValue("startDate", null, "can not be before current date");
        }

        if (eventForm.getImage() != null) {
            var imageValidationResult = ImageUtils.isValid(eventForm.getImage(), 5 * 1024 * 1024, 675, 1200);
            if (!imageValidationResult.getData()) {
                errors.rejectValue("image", null, imageValidationResult.getMessage());
            }
        }
    }
}
