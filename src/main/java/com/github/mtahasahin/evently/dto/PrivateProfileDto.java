package com.github.mtahasahin.evently.dto;

import com.github.mtahasahin.evently.interfaces.Profile;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PrivateProfileDto implements Profile {
    private String username;
    private String name;
    private LocalDateTime registrationDate;
    private Boolean isProfilePublic;
    private String about;
    private int followersCount;
    private int followingsCount;
    private Boolean isFollowing;
    private Boolean hasFollowingRequest;
}