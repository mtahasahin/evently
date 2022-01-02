package com.github.mtahasahin.evently.exception;

import java.util.UUID;

public class EventApplicationNotFoundException extends RuntimeException {
    public EventApplicationNotFoundException(UUID id) {
        super("Could not find event application with id: " + id);
    }

    public EventApplicationNotFoundException(String message) {
        super(message);
    }
}
