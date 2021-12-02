package com.github.mtahasahin.evently.exception;

public class EventIsAlreadyOverException extends RuntimeException {

    public EventIsAlreadyOverException() {
        super("Event is already over");
    }

    public EventIsAlreadyOverException(String message) {
        super(message);
    }
}
