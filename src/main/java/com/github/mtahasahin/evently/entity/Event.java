package com.github.mtahasahin.evently.entity;

import com.github.mtahasahin.evently.domainevent.GoingEventEvent;
import com.github.mtahasahin.evently.enums.EventLocationType;
import com.github.mtahasahin.evently.enums.EventVisibility;
import com.github.mtahasahin.evently.validator.Language;
import com.github.mtahasahin.evently.validator.TimeZone;
import lombok.*;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Indexed(index = "event_index")
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

    @Transient
    private List<Object> domainEvents = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @IndexedEmbedded
    @ManyToOne(optional = false)
    @JoinColumn(name = "ORGANIZER_ID")
    private AppUser organizer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "event")
    private Set<EventQuestion> eventQuestions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "event")
    @IndexedEmbedded
    private Set<EventApplication> eventApplications = new HashSet<>();
    @FullTextField(analyzer = "english")
    @NotBlank
    private String name;
    @KeywordField
    @NotBlank
    private String slug;
    @KeywordField
    @TimeZone
    private String timezone;
    @GenericField(sortable = Sortable.YES)
    @NotNull
    private LocalDateTime startDate;
    @GenericField
    @NotNull
    private LocalDateTime endDate;
    @KeywordField
    private String imagePath;
    @FullTextField(analyzer = "english")
    @NotBlank
    @Column(length = 2000)
    private String description;
    @GenericField
    private EventVisibility visibility;

    @Transient
    @GenericField
    @IndexingDependency(derivedFrom = {@ObjectPath({@PropertyValue(propertyName = "eventApplications")})})
    public int getAttendeeCount() {
        return (int) eventApplications.stream().filter(EventApplication::isConfirmed).count() + 1;
    }

    public void addEventApplication(EventApplication eventApplication) {
        eventApplications.add(eventApplication);
        eventApplication.setEvent(this);
        eventApplication.getApplicant().getEventApplications().add(eventApplication);

        if (eventApplication.isConfirmed()) {
            domainEvents.add(new GoingEventEvent(eventApplication.getApplicant().getId(), this.getId()));
        }
    }

    @NotNull
    private EventLocationType eventLocationType;
    private String location;
    private String eventUrl;
    @Language
    @GenericField
    private String language;

    public void addEventQuestion(EventQuestion eventQuestion) {
        eventQuestions.add(eventQuestion);
        eventQuestion.setEvent(this);
    }

    private String key;
    private boolean limited;
    private int attendeeLimit;
    private boolean approvalRequired;

    @DomainEvents
    public List<Object> getDomainEvents() {
        return domainEvents;
    }
}
