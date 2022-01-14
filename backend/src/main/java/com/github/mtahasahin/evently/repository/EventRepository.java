package com.github.mtahasahin.evently.repository;

import com.github.mtahasahin.evently.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    Optional<Event> findBySlug(String slug);

    Slice<Event> getEventsByOrganizerIdAndStartDateAfterOrderByStartDateAsc(UUID organizedId, LocalDateTime dateTime, Pageable pageable);
    Slice<Event> getEventsByOrganizerIdAndStartDateBeforeOrderByStartDateDesc(UUID organizedId, LocalDateTime dateTime, Pageable pageable);
}
