package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.dto.CreateUpdateEventForm;
import com.github.mtahasahin.evently.dto.EventQuestionAnswerDto;
import com.github.mtahasahin.evently.dto.EventQuestionDto;
import com.github.mtahasahin.evently.entity.EventQuestionAnswer;
import com.github.mtahasahin.evently.service.EventApplicationService;
import com.github.mtahasahin.evently.service.EventService;
import com.github.mtahasahin.evently.validator.EventFormValidator;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Set;

@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/api/event")
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
    public ResponseEntity<?> getEvent(Authentication authentication, @PathVariable String slug, @RequestParam(required = false) String key) {
        return new ResponseEntity<>(eventService.getEvent(Long.parseLong(authentication.getName()), slug, key), HttpStatus.OK);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createEvent(Authentication authentication, @Valid @ModelAttribute("form") CreateUpdateEventForm form) {
        return new ResponseEntity<>(eventService.createEvent(Long.parseLong(authentication.getName()), form), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{slug}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateEvent(Authentication authentication, @PathVariable String slug, @Valid @ModelAttribute("form") CreateUpdateEventForm form) {
        return new ResponseEntity<>(eventService.updateEvent(Long.parseLong(authentication.getName()), slug, form), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{slug}")
    public ResponseEntity<?> removeEvent(Authentication authentication, @PathVariable String slug) {
        eventService.removeEvent(Long.parseLong(authentication.getName()), slug);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/{slug}/questions")
    public ResponseEntity<?> updateEventQuestions(Authentication authentication, @PathVariable String slug,@RequestBody ArrayList<@Valid EventQuestionDto> questions) {
        return new ResponseEntity<>(eventService.updateEventQuestions(Long.parseLong(authentication.getName()), slug, questions), HttpStatus.OK);
    }

    @GetMapping(path = "/{slug}/answers")
    public ResponseEntity<?> getAnswers(Authentication authentication, @PathVariable String slug, @RequestParam int page) {
        return new ResponseEntity<>(eventApplicationService.getAnswers(Long.parseLong(authentication.getName()), slug, page, false), HttpStatus.OK);
    }

    @GetMapping(path = "/{slug}/answers/{applicationId}")
    public ResponseEntity<?> getAnswer(Authentication authentication, @PathVariable String slug, @PathVariable Long applicationId) {
        return new ResponseEntity<>(eventApplicationService.getAnswer(Long.parseLong(authentication.getName()), slug, applicationId), HttpStatus.OK);
    }

    @GetMapping(path = "/{slug}/answers/all")
    public ResponseEntity<?> getAllAnswers(Authentication authentication, @PathVariable String slug, @RequestParam int page) {
        return new ResponseEntity<>(eventApplicationService.getAnswers(Long.parseLong(authentication.getName()), slug, page, true), HttpStatus.OK);
    }

    @PostMapping(path = "/{slug}/answers")
    public ResponseEntity<?> applyEvent(Authentication authentication, @PathVariable String slug, @RequestBody(required = false) Set<EventQuestionAnswerDto> answers) {
        var result = eventApplicationService.applyToEvent(Long.parseLong(authentication.getName()), slug, answers);
        return new ResponseEntity<>(ApiResponse.Success(result, "Applied to event"),HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{slug}/answers")
    public ResponseEntity<?> cancelEventApplication(Authentication authentication, @PathVariable String slug) {
        eventApplicationService.cancelEventApplication(Long.parseLong(authentication.getName()), slug);
        return new ResponseEntity<>(ApiResponse.Success(null, "Cancelled application"),HttpStatus.OK);
    }

    @PostMapping(path = "/{slug}/answers/{applicationId}/approve")
    public ResponseEntity<?> confirmApplication(Authentication authentication, @PathVariable String slug, @PathVariable Long applicationId) {
        eventApplicationService.confirmApplication(Long.parseLong(authentication.getName()), slug, applicationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
