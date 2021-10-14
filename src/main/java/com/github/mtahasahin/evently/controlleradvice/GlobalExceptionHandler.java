package com.github.mtahasahin.evently.controlleradvice;

import com.github.mtahasahin.evently.wrapper.ApiResponse;
import com.github.mtahasahin.evently.exception.EmailAlreadyTakenException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = EmailAlreadyTakenException.class)
    public ResponseEntity<ApiResponse<Object>> emailAlreadyTakenException(EmailAlreadyTakenException emailAlreadyTakenException) {
        return new ResponseEntity<>(ApiResponse.Error(null, "This email has already been taken"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ApiResponse.Error(null,
                ex.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<Object>> databaseConnectionFailsException(Exception exception) {
        return new ResponseEntity<>(ApiResponse.Error(null, exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
