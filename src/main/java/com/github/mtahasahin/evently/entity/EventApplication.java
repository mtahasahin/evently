package com.github.mtahasahin.evently.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
    private Set<EventQuestionAnswer> answers = new HashSet<>();
}
