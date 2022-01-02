package com.github.mtahasahin.evently.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "EVENT_APPLICATIONS", indexes = {
        @Index(columnList = "EVENT_ID, USER_ID")
})
public class EventApplication extends Auditable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID")
    private AppUser applicant;

    private boolean confirmed;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
    private Set<EventQuestionAnswer> answers = new HashSet<>();
}
