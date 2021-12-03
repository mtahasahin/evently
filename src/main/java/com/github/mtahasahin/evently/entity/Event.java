package com.github.mtahasahin.evently.entity;

import com.github.mtahasahin.evently.enums.EventLocationType;
import com.github.mtahasahin.evently.enums.EventVisibility;
import com.github.mtahasahin.evently.validator.Language;
import com.github.mtahasahin.evently.validator.TimeZone;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
public class Event extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ORGANIZER_ID")
    private AppUser organizer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "event")
    private Set<EventQuestion> eventQuestions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "event")
    private Set<EventApplication> eventApplications = new HashSet<>();

    public int getAttendeeCount(){
        return (int) eventApplications.stream().filter(EventApplication::isConfirmed).count() + 1;
    }

    public void addEventApplication(EventApplication eventApplication){
        eventApplications.add(eventApplication);
        eventApplication.setEvent(this);
        eventApplication.getApplicant().getEventApplications().add(eventApplication);
    }

    public void addEventQuestion(EventQuestion eventQuestion){
        eventQuestions.add(eventQuestion);
        eventQuestion.setEvent(this);
    }

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
