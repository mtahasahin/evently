package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.dto.UpcomingEventsRequest;
import com.github.mtahasahin.evently.service.UpcomingEventsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping(path = "/api/upcoming")
@RequiredArgsConstructor
@RestController
public class UpcomingEventsController {
    private final UpcomingEventsService upcomingEventsService;

    @PostMapping
    public ResponseEntity getUpcomingEvents(@RequestBody @Valid UpcomingEventsRequest request) {
        return ResponseEntity.ok(upcomingEventsService.getUpcomingEvents(request));
    }

    @PostMapping("/getRandomEvents")
    public ResponseEntity getRandomEvents() {
        return ResponseEntity.ok(upcomingEventsService.getRandomEvents());
    }

}
