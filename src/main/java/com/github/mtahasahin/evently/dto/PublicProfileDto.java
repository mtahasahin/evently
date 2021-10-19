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
    private Boolean isProfilePublic;
    private String about;
    private String websiteUrl;
    private String twitterUsername;
    private String facebookUsername;
    private String instagramUsername;
    private String githubUsername;
    private int followersCount;
    private int followingsCount;
    private Boolean isFollowing;
    private Boolean hasFollowingRequest;
}
