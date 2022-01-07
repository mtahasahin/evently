package com.github.mtahasahin.evently.eventlistener;

import com.github.mtahasahin.evently.domainevent.AcceptMultipleFriendRequestsEvent;
import com.github.mtahasahin.evently.entity.Activity;
import com.github.mtahasahin.evently.enums.ActivityType;
import com.github.mtahasahin.evently.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class AcceptMultipleFriendRequestsEventListener {
    private final ActivityRepository activityRepository;

    @EventListener
    public void handleAcceptMultipleFriendRequestsEvent(AcceptMultipleFriendRequestsEvent event) {
        var activities = new ArrayList<Activity>(event.getFollowerIds().size());
        event.getFollowerIds().forEach(followerId -> {
            activities.add(Activity.builder().activityType(ActivityType.FOLLOWED_USER).userId(followerId).objectId(event.getUserId()).build());
        });
        activityRepository.saveAll(activities);
    }

}
