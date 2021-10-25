package com.github.mtahasahin.evently.entity;

import com.github.mtahasahin.evently.validator.TimeZone;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "USERPROFILES")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private LocalDate dateOfBirth;
    private LocalDateTime registrationDate;
    private boolean profilePublic;
    @Length(max = 40)
    private String location;
    @TimeZone
    @Length(max = 40)
    private String timezone;
    @Length(max = 40)
    private String language;
    private String about;
    private String websiteUrl;
    private String twitterUsername;
    private String facebookUsername;
    private String instagramUsername;
    private String githubUsername;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "USER_ID")
    private AppUser user;
}
