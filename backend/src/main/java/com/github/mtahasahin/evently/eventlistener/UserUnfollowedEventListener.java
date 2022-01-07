package com.github.mtahasahin.evently.eventlistener;

import com.github.mtahasahin.evently.domainevent.UserUnfollowedEvent;
import com.github.mtahasahin.evently.enums.ActivityType;
import com.github.mtahasahin.evently.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUnfollowedEventListener {
    private final ActivityRepository activityRepository;

    @EventListener
    public void handleUserUnfollowedEvent(UserUnfollowedEvent event) {
        activityRepository.deleteActivityByUserIdAndObjectIdAndActivityType(event.getFollowerId(), event.getFollowingId(), ActivityType.FOLLOWED_USER);
    }

}
