package com.github.mtahasahin.evently.service;

import com.github.mtahasahin.evently.dto.ActivityDto;
import com.github.mtahasahin.evently.entity.Activity;
import com.github.mtahasahin.evently.enums.ActivityType;
import com.github.mtahasahin.evently.exception.CustomAccessDeniedException;
import com.github.mtahasahin.evently.repository.ActivityRepository;
import com.github.mtahasahin.evently.repository.FollowerFollowingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final EventService eventService;
    private final UserService userService;
    private final FollowerFollowingRepository followerFollowingRepository;

    public List<ActivityDto> getFriendActivities(Long userId) {
        var friendIdList = followerFollowingRepository.getFriendsIds(userId);
        friendIdList.add(userId);

        var activities = activityRepository.getActivitiesByUserId(friendIdList, Pageable.ofSize(20));

        return getActivities(activities.getContent());
    }

    public List<ActivityDto> getUserActivities(Long requestingUserId, String requestedUserName) {
        if (!userService.canSeeProfile(requestingUserId, requestedUserName)) {
            throw new CustomAccessDeniedException();
        }
        return getActivitiesByUserId(userService.getUser(requestedUserName).getId());
    }

    private List<ActivityDto> getActivitiesByUserId(Long userId) {
        var activities = activityRepository.getActivitiesByUserId(userId, Pageable.ofSize(20));
        return getActivities(activities.getContent());
    }

    private List<ActivityDto> getActivities(List<Activity> activities) {
        var eventsToFetch = activities
                .stream()
                .filter(e -> e.getActivityType() == ActivityType.GOING_EVENT || e.getActivityType() == ActivityType.CREATED_EVENT)
                .map(Activity::getObjectId)
                .distinct()
                .collect(Collectors.toList());

        var usersToFetch = Stream.concat(
                        activities
                                .stream()
                                .filter(e -> e.getActivityType() == ActivityType.FOLLOWED_USER)
                                .map(Activity::getObjectId)
                                .distinct(),
                        activities
                                .stream()
                                .map(Activity::getUserId)
                                .distinct())
                .distinct()
                .collect(Collectors.toList());


        var events = eventService.getEventsById(eventsToFetch);

        var users = userService.getUsersById(usersToFetch);

        List<ActivityDto> result = new ArrayList<>(activities.size());

        activities.forEach(a -> {
            if (a.getActivityType() == ActivityType.GOING_EVENT || a.getActivityType() == ActivityType.CREATED_EVENT) {
                result.add(ActivityDto.builder()
                        .id(a.getId())
                        .activityType(a.getActivityType())
                        .user(users.stream().filter(e -> Objects.equals(e.getId(), a.getUserId())).findFirst().get())
                        .event(events.stream().filter(e -> Objects.equals(e.getId(), a.getObjectId())).findFirst().get())
                        .createdAt(a.getCreatedDate())
                        .build());
            } else if (a.getActivityType() == ActivityType.FOLLOWED_USER) {
                result.add(ActivityDto.builder()
                        .id(a.getId())
                        .activityType(a.getActivityType())
                        .user(users.stream().filter(e -> Objects.equals(e.getId(), a.getUserId())).findFirst().get())
                        .followingUser(users.stream().filter(u -> Objects.equals(u.getId(), a.getObjectId())).findFirst().get())
                        .createdAt(a.getCreatedDate())
                        .build());
            }
        });

        return result;
    }
}
