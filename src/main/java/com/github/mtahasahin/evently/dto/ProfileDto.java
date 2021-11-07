package com.github.mtahasahin.evently.dto;

import com.github.mtahasahin.evently.validator.TimeZone;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class ProfileDto {
    @NotBlank(message = "Name field can not be empty")
    @Length(min = 2, message = "Name should be at least 2 characters long")
    private String name;
    @NotNull
    private LocalDate dateOfBirth;
    private boolean profilePublic;
    @TimeZone
    private String timezone;
    private String location;
    @NotBlank
    private String language;
    private String about;
    private String websiteUrl;
    private String twitterUsername;
    private String facebookUsername;
    private String instagramUsername;
    private String githubUsername;
}
