package com.github.mtahasahin.evently.entity;

import com.github.mtahasahin.evently.enums.EventQuestionType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "EVENTQUESTIONS", indexes = {
        @Index(columnList = "EVENT_ID")
})
public class EventQuestion extends Auditable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    @OneToMany(mappedBy = "question", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<EventQuestionAnswer> answers;

    private EventQuestionType type;

    @Column(name = "QUESTION_ORDER")
    private int order;

    private String title;

    private String description;

    private boolean required;
}
