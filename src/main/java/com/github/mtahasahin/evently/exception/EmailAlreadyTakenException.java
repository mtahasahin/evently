package com.github.mtahasahin.evently.exception;


import org.springframework.security.core.AuthenticationException;

public class EmailAlreadyTakenException extends AuthenticationException {

    public EmailAlreadyTakenException(final String msg) {
        super(msg);
    }

}
