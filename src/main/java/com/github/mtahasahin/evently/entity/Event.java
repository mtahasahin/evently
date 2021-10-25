package com.github.mtahasahin.evently.entity;

import com.github.mtahasahin.evently.enums.EventLocationType;
import com.github.mtahasahin.evently.enums.EventVisibility;
import com.github.mtahasahin.evently.validator.Language;
import com.github.mtahasahin.evently.validator.TimeZone;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "EVENTS", indexes = {
        @Index(columnList = "SLUG", unique = true),
        @Index(columnList = "ORGANIZER_ID"),
})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ORGANIZER_ID")
    private AppUser organizer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "event")
    private Set<EventQuestion> eventQuestions = new HashSet<>();

    @NotBlank
    private String name;
    @NotBlank
    private String slug;

    @TimeZone
    private String timezone;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;

    @NotBlank
    private String imagePath;
    @NotBlank
    private String description;

    @NotNull
    private EventLocationType eventLocationType;
    private String location;
    private String eventUrl;
    @Language
    private String language;

    private EventVisibility visibility;
    private String key;
    private boolean limited;
    private int attendeeLimit;
    private boolean approvalRequired;
}