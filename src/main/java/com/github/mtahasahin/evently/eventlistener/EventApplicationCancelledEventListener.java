package com.github.mtahasahin.evently.eventlistener;

import com.github.mtahasahin.evently.domainevent.EventApplicationCancelledEvent;
import com.github.mtahasahin.evently.enums.ActivityType;
import com.github.mtahasahin.evently.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EventApplicationCancelledEventListener {
    private final ActivityRepository activityRepository;

    @EventListener
    public void handleEventApplicationCancelledEvent(EventApplicationCancelledEvent event) {
        activityRepository.deleteActivityByUserIdAndObjectIdAndActivityType(event.getUserId(), event.getEventId(), ActivityType.GOING_EVENT);
    }

}
