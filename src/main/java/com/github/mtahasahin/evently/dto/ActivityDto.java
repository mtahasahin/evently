package com.github.mtahasahin.evently.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mtahasahin.evently.enums.ActivityType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ActivityDto {
    private Long id;
    private UserLightDto user;
    @JsonProperty("activity_type")
    private ActivityType activityType;
    private EventDto event;
    @JsonProperty("following_user")
    private UserLightDto followingUser;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("status_text")
    public String getStatusText() {
        switch (this.activityType) {
            case FOLLOWED_USER:
                return "followed";
            case CREATED_EVENT:
                return "created an event";
            case GOING_EVENT:
                return "going to an event";
            default:
                throw new IllegalArgumentException("Unknown activity type");
        }
    }
}
