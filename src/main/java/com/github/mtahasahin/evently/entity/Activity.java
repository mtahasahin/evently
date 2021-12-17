package com.github.mtahasahin.evently.entity;

import com.github.mtahasahin.evently.enums.ActivityType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(indexes = {@Index(columnList = "user_id, last_modified_date")})
public class Activity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "activity_type", nullable = false)
    private ActivityType activityType;

    @Column(name = "object_id")
    private Long objectId;

}
