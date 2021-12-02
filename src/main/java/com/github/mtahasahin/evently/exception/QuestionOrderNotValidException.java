package com.github.mtahasahin.evently.exception;

public class QuestionOrderNotValidException extends RuntimeException {
    public QuestionOrderNotValidException() {
        super("Question order is not valid");
    }
}
