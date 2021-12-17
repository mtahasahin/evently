package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.dto.ActivityDto;
import com.github.mtahasahin.evently.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<List<ActivityDto>> getActivityFeed(Authentication authentication) {
        return ResponseEntity.ok(activityService.getFriendActivities(Long.valueOf(authentication.getName())));
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<ActivityDto>> getUserActivities(Authentication authentication, @PathVariable String username) {
        return ResponseEntity.ok(activityService.getUserActivities(Long.valueOf(authentication.getName()), username));
    }
}
