package com.github.mtahasahin.evently.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(final String msg) {
        super(msg);
    }

}
