package com.github.mtahasahin.evently.eventlistener;

import com.github.mtahasahin.evently.domainevent.EventCreatedEvent;
import com.github.mtahasahin.evently.entity.Activity;
import com.github.mtahasahin.evently.enums.ActivityType;
import com.github.mtahasahin.evently.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EventCreatedEventListener {
    private final ActivityRepository activityRepository;

    @EventListener
    public void handleEventCreatedEvent(EventCreatedEvent event) {
        activityRepository.save(Activity.builder()
                .userId(event.getUserId())
                .objectId(event.getEventId())
                .activityType(ActivityType.CREATED_EVENT)
                .build());
    }

}
