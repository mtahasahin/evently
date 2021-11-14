package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.dto.CreateUpdateEventForm;
import com.github.mtahasahin.evently.service.EventService;
import com.github.mtahasahin.evently.validator.EventFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping(path = "/api/event")
@RestController
public class EventController {
    private final EventService eventService;
    private final EventFormValidator eventFormValidator;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(eventFormValidator);
    }

    @GetMapping(path = "/{slug}")
    public ResponseEntity<?> getEvent(Authentication authentication, @PathVariable String slug, @RequestParam(required = false) String key) {
        return new ResponseEntity<>(eventService.getEvent(Long.parseLong(authentication.getName()), slug, key), HttpStatus.OK);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createEvent(Authentication authentication, @Valid @ModelAttribute CreateUpdateEventForm form) {
        return new ResponseEntity<>(eventService.createEvent(Long.parseLong(authentication.getName()), form), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{slug}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateEvent(Authentication authentication, @PathVariable String slug, @Valid @ModelAttribute CreateUpdateEventForm form) {
        return new ResponseEntity<>(eventService.updateEvent(Long.parseLong(authentication.getName()), slug, form), HttpStatus.OK);
    }
}
