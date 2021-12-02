package com.github.mtahasahin.evently.repository;

import com.github.mtahasahin.evently.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findBySlug(String slug);
}
