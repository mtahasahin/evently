package com.github.mtahasahin.evently.repository;

import com.github.mtahasahin.evently.entity.EventApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventApplicationRepository extends JpaRepository<EventApplication, UUID> {
    Optional<EventApplication> findByEventSlugAndId(String slug, UUID id);
    Optional<EventApplication> findByEventSlugAndApplicantId(String slug, UUID applicantId);
    Page<EventApplication> findAllByEventIdOrderByCreatedDateDesc(UUID eventId, Pageable pageable);
    @Query("select e from EventApplication e where e.event.id = ?1 and (e.lastModifiedDate > ?2 or e.confirmed = false) order by e.createdDate DESC")
    Page<EventApplication> findNotConfirmedEventApplications(UUID event_id, LocalDateTime lastModifiedDate, Pageable pageable);
    @Query("SELECT applicant.id FROM EventApplication WHERE event.id = ?1 AND confirmed = true")
    Slice<UUID> getAllApplicants(UUID event_id, Pageable pageable);
}
