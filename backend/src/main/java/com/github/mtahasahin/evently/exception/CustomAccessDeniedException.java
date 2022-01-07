package com.github.mtahasahin.evently.exception;

public class CustomAccessDeniedException extends RuntimeException {
    public CustomAccessDeniedException(String message) {
        super(message);
    }

    public CustomAccessDeniedException() {
        super("Access denied");
    }

}
