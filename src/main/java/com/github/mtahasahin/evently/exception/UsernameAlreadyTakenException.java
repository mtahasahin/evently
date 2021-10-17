package com.github.mtahasahin.evently.exception;


public class UsernameAlreadyTakenException extends RuntimeException {

    public UsernameAlreadyTakenException(final String msg) {
        super(msg);
    }

}
