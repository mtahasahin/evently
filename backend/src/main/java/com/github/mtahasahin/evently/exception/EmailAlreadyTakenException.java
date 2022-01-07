package com.github.mtahasahin.evently.exception;


public class EmailAlreadyTakenException extends RuntimeException {

    public EmailAlreadyTakenException(String msg) {
        super(msg);
    }

}
