package com.github.mtahasahin.evently.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
abstract class Auditable {

    @CreatedDate
    protected LocalDateTime createdDate;

    @LastModifiedDate
    protected LocalDateTime lastModifiedDate;
}