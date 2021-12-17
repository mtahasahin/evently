package com.github.mtahasahin.evently.eventlistener;

import com.github.mtahasahin.evently.domainevent.EventDeletedEvent;
import com.github.mtahasahin.evently.enums.ActivityType;
import com.github.mtahasahin.evently.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EventDeletedEventListener {
    private final ActivityRepository activityRepository;

    @EventListener
    public void handleEventDeletedEvent(EventDeletedEvent event) {
        activityRepository.deleteActivityByUserIdAndObjectIdAndActivityType(event.getUserId(), event.getEventId(), ActivityType.CREATED_EVENT);
    }

}
