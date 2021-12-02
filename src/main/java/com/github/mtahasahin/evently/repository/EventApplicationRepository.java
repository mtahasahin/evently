package com.github.mtahasahin.evently.repository;

import com.github.mtahasahin.evently.entity.EventApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EventApplicationRepository extends JpaRepository<EventApplication, Long> {
    Optional<EventApplication> findByEventSlugAndId(String slug, Long id);
    Optional<EventApplication> findByEventSlugAndApplicantId(String slug, Long applicantId);
    Page<EventApplication> findAllByEventIdOrderByCreatedAtDesc(Long eventId, Pageable pageable);
    Page<EventApplication> findAllByEventIdAndUpdatedAtGreaterThanOrConfirmedIsFalseOrderByCreatedAtDesc(Long event_id, LocalDateTime updatedAt, Pageable pageable);
}
