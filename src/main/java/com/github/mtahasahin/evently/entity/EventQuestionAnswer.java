package com.github.mtahasahin.evently.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EVENT_QUESTION_ANSWERS", indexes = {
        @Index(columnList = "EVENT_APPLICATION_ID, QUESTION_ID")
})
public class EventQuestionAnswer extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private EventQuestion question;

    @ManyToOne
    @JoinColumn(name = "EVENT_APPLICATION_ID")
    private EventApplication application;

    private String answer;
}
