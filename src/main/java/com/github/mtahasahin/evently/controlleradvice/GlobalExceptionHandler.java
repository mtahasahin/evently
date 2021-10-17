package com.github.mtahasahin.evently.controlleradvice;

import com.github.mtahasahin.evently.exception.UserNotFoundException;
import com.github.mtahasahin.evently.exception.UsernameAlreadyTakenException;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import com.github.mtahasahin.evently.exception.EmailAlreadyTakenException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = EmailAlreadyTakenException.class)
    public ResponseEntity<ApiResponse<Object>> emailAlreadyTakenException(EmailAlreadyTakenException emailAlreadyTakenException) {
        return new ResponseEntity<>(ApiResponse.Error(null, emailAlreadyTakenException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UsernameAlreadyTakenException.class)
    public ResponseEntity<ApiResponse<Object>> usernameAlreadyTakenException(UsernameAlreadyTakenException usernameAlreadyTakenException) {
        return new ResponseEntity<>(ApiResponse.Error(null, usernameAlreadyTakenException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> UserNotFoundException(UserNotFoundException userNotFoundException) {
        return new ResponseEntity<>(ApiResponse.Error(null, userNotFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ApiResponse.Error(null,
                ex.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> badCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(ApiResponse.Error(null, "Email and password do not match"),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<Object>> databaseConnectionFailsException(Exception exception) {
        return new ResponseEntity<>(ApiResponse.Error(null, exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
