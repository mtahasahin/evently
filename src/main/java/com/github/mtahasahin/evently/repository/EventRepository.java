package com.github.mtahasahin.evently.repository;

import com.github.mtahasahin.evently.entity.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EventRepository extends CrudRepository<Event, Long> {

    Optional<Event> findBySlug(String slug);
}
