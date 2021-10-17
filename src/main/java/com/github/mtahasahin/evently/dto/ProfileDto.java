package com.github.mtahasahin.evently.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ProfileDto {
    @NotBlank
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
}
