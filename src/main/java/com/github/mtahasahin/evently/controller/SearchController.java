package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.dto.SearchRequest;
import com.github.mtahasahin.evently.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping(path = "/api/search")
@RequiredArgsConstructor
@RestController
public class SearchController {
    private final SearchService searchService;

    @PostMapping
    public ResponseEntity search(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(searchService.search(request));
    }

    @PostMapping("/events")
    public ResponseEntity searchEvent(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(searchService.searchEvent(request));
    }

    @PostMapping("/users")
    public ResponseEntity searchUser(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(searchService.searchUser(request));
    }


}
