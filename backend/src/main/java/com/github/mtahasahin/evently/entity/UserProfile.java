package com.github.mtahasahin.evently.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "USERPROFILES")
public class UserProfile extends Auditable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    private UUID id;

    @FullTextField(analyzer = "name")
    private String name;
    private LocalDate dateOfBirth;
    private LocalDateTime registrationDate;
    private boolean profilePublic;
    @Column(length = 50)
    private String location;
    @Column(length = 50)
    private String timezone;
    @Column(length = 50)
    private String language;
    @FullTextField(analyzer = "english")
    @Column(length = 500)
    private String about;
    @KeywordField
    private String avatar;
    private String websiteUrl;
    private String twitterUsername;
    private String facebookUsername;
    private String instagramUsername;
    private String githubUsername;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "USER_ID")
    private AppUser user;
}
