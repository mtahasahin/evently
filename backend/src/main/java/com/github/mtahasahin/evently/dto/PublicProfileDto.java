package com.github.mtahasahin.evently.dto;

import com.github.mtahasahin.evently.interfaces.Profile;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class PublicProfileDto implements Profile {
    private String username;
    private String name;
    private LocalDate dateOfBirth;
    private LocalDateTime registrationDate;
    private boolean profilePublic;
    private String about;
    private String avatar;
    private String websiteUrl;
    private String twitterUsername;
    private String facebookUsername;
    private String instagramUsername;
    private String githubUsername;
    private int followersCount;
    private int followingCount;
    private int activityCount;
    private boolean following;
    private boolean hasFollowingRequest;
    private boolean canEdit;
}
