package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.dto.CreateUpdateEventForm;
import com.github.mtahasahin.evently.dto.EventQuestionAnswerDto;
import com.github.mtahasahin.evently.dto.EventQuestionDto;
import com.github.mtahasahin.evently.dto.validationgroups.CreateEventValidationGroup;
import com.github.mtahasahin.evently.dto.validationgroups.UpdateEventValidationGroup;
import com.github.mtahasahin.evently.service.EventApplicationService;
import com.github.mtahasahin.evently.service.EventService;
import com.github.mtahasahin.evently.validator.EventFormValidator;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/api/event")
@PreAuthorize("isAuthenticated()")
@RestController
public class EventController {
    private final EventService eventService;
    private final EventApplicationService eventApplicationService;
    private final EventFormValidator eventFormValidator;

    @InitBinder({"form"})
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(eventFormValidator);
    }

    @GetMapping(path = "/{slug}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getEvent(Authentication authentication, @PathVariable String slug, @RequestParam(required = false) String key) {
        return new ResponseEntity<>(eventService.getEvent(authentication != null ? UUID.fromString(authentication.getName()) : null, slug, key), HttpStatus.OK);
    }

    @PostMapping(consumes = "multipart/form-data")
    @Validated(CreateEventValidationGroup.class)
    public ResponseEntity<?> createEvent(Authentication authentication, @Valid @ModelAttribute("form") CreateUpdateEventForm form) {
        return new ResponseEntity<>(eventService.createEvent(UUID.fromString(authentication.getName()), form), HttpStatus.CREATED);
    }

    @PostMapping(path = "/{slug}", consumes = "multipart/form-data")
    @Validated(UpdateEventValidationGroup.class)
    public ResponseEntity<?> updateEvent(Authentication authentication, @PathVariable String slug, @Valid @ModelAttribute("form") CreateUpdateEventForm form) {
        return new ResponseEntity<>(eventService.updateEvent(UUID.fromString(authentication.getName()), slug, form), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{slug}")
    public ResponseEntity<?> removeEvent(Authentication authentication, @PathVariable String slug) {
        eventService.removeEvent(UUID.fromString(authentication.getName()), slug);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(path = "/{slug}/attendees")
    public ResponseEntity<?> getEventAttendees(Authentication authentication, @PathVariable String slug, @RequestParam(required = false) String key, @RequestParam int page, @RequestParam int limit) {
        return new ResponseEntity<>(eventService.getEventAttendees(authentication != null ? UUID.fromString(authentication.getName()) : null, slug, key, page, limit), HttpStatus.OK);
    }

    @PutMapping(path = "/{slug}/questions")
    public ResponseEntity<?> updateEventQuestions(Authentication authentication, @PathVariable String slug,@RequestBody ArrayList<@Valid EventQuestionDto> questions) {
        return new ResponseEntity<>(eventService.updateEventQuestions(UUID.fromString(authentication.getName()), slug, questions), HttpStatus.OK);
    }

    @GetMapping(path = "/{slug}/answers")
    public ResponseEntity<?> getAnswers(Authentication authentication, @PathVariable String slug, @RequestParam int page) {
        return new ResponseEntity<>(eventApplicationService.getAnswers(UUID.fromString(authentication.getName()), slug, page, false), HttpStatus.OK);
    }

    @GetMapping(path = "/{slug}/answers/{applicationId:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$}")
    public ResponseEntity<?> getAnswer(Authentication authentication, @PathVariable String slug, @PathVariable UUID applicationId) {
        return new ResponseEntity<>(eventApplicationService.getAnswer(UUID.fromString(authentication.getName()), slug, applicationId), HttpStatus.OK);
    }

    @GetMapping(path = "/{slug}/answers/all")
    public ResponseEntity<?> getAllAnswers(Authentication authentication, @PathVariable String slug, @RequestParam int page) {
        return new ResponseEntity<>(eventApplicationService.getAnswers(UUID.fromString(authentication.getName()), slug, page, true), HttpStatus.OK);
    }

    @PostMapping(path = "/{slug}/answers")
    public ResponseEntity<?> applyEvent(Authentication authentication, @PathVariable String slug, @RequestBody(required = false) Set<EventQuestionAnswerDto> answers) {
        var result = eventApplicationService.applyToEvent(UUID.fromString(authentication.getName()), slug, answers);
        return new ResponseEntity<>(ApiResponse.Success(result, "Applied to event"),HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{slug}/answers")
    public ResponseEntity<?> cancelEventApplication(Authentication authentication, @PathVariable String slug) {
        eventApplicationService.cancelEventApplication(UUID.fromString(authentication.getName()), slug);
        return new ResponseEntity<>(ApiResponse.Success(null, "Cancelled application"),HttpStatus.OK);
    }

    @PostMapping(path = "/{slug}/answers/{applicationId}/approve")
    public ResponseEntity<?> confirmApplication(Authentication authentication, @PathVariable String slug, @PathVariable UUID applicationId) {
        eventApplicationService.confirmApplication(UUID.fromString(authentication.getName()), slug, applicationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
