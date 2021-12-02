package com.github.mtahasahin.evently.exception;

public class UserHasAlreadyAppliedToEventException extends RuntimeException{

    public UserHasAlreadyAppliedToEventException() {
        super("User has already applied to this event");
    }

}
