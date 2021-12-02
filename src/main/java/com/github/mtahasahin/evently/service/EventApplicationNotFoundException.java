package com.github.mtahasahin.evently.service;

public class EventApplicationNotFoundException extends RuntimeException {
    public EventApplicationNotFoundException(long id) {
        super("Could not find event application with id: " + id);
    }

    public EventApplicationNotFoundException(String message) {
        super(message);
    }
}
