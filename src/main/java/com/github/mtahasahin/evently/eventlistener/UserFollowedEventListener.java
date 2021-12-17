package com.github.mtahasahin.evently.eventlistener;

import com.github.mtahasahin.evently.domainevent.UserFollowedEvent;
import com.github.mtahasahin.evently.entity.Activity;
import com.github.mtahasahin.evently.enums.ActivityType;
import com.github.mtahasahin.evently.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserFollowedEventListener {
    private final ActivityRepository activityRepository;

    @EventListener
    public void handleUserFollowedEvent(UserFollowedEvent event) {
        activityRepository.save(Activity.builder()
                .userId(event.getFollowerId())
                .objectId(event.getFollowingId())
                .activityType(ActivityType.FOLLOWED_USER)
                .build());
    }

}
