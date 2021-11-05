package com.github.mtahasahin.evently.exception;

import com.github.mtahasahin.evently.wrapper.ApiResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class CustomValidationException extends RuntimeException {
    private final List<ApiResponse.ApiSubError> errors;

    public CustomValidationException(List<ApiResponse.ApiSubError> errors) {
        this("Some fields are invalid.", errors);
    }

    public CustomValidationException(ApiResponse.ApiSubError error) {
        this(List.of(error));
    }

    public CustomValidationException(String msg, ApiResponse.ApiSubError error) {
        this(msg, List.of(error));
    }

    public CustomValidationException(String msg, List<ApiResponse.ApiSubError> errors) {
        super(msg);
        this.errors = errors;
    }
}
