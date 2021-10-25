package com.github.mtahasahin.evently.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "EVENT_APPLICATIONS", indexes = {
        @Index(columnList = "EVENT_ID, USER_ID")
})
public class EventApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID")
    private AppUser applicant;

    private boolean confirmed;

    @OneToMany(mappedBy = "application")
    private Set<EventQuestionAnswer> answers = new HashSet<>();
}
