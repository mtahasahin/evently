package com.github.mtahasahin.evently.controlleradvice;

import com.github.mtahasahin.evently.exception.CustomValidationException;
import com.github.mtahasahin.evently.exception.EmailAlreadyTakenException;
import com.github.mtahasahin.evently.exception.UserNotFoundException;
import com.github.mtahasahin.evently.exception.UsernameAlreadyTakenException;
import com.github.mtahasahin.evently.wrapper.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
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
        return new ResponseEntity<>(ApiResponse.Error(null, "Some fields are invalid.",
                ex.getBindingResult().getFieldErrors().stream()
                        .map(e -> new ApiResponse.ApiSubError(e.getField(), e.getDefaultMessage()))
                        .collect(Collectors.toList())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(BindException ex) {
        return new ResponseEntity<>(ApiResponse.Error(null, "Some fields are invalid.",
                ex.getBindingResult().getFieldErrors().stream()
                        .map(e -> new ApiResponse.ApiSubError(e.getField(), e.getDefaultMessage()))
                        .collect(Collectors.toList())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(ConstraintViolationException ex) {
        return new ResponseEntity<>(ApiResponse.Error(null, "Some fields are invalid.",
                ex.getConstraintViolations().stream()
                        .map(e -> new ApiResponse.ApiSubError(e.getPropertyPath().toString(), e.getMessage()))
                        .collect(Collectors.toList())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(CustomValidationException ex) {
        return new ResponseEntity<>(ApiResponse.Error(null, ex.getMessage(),
                ex.getErrors()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> badCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(ApiResponse.Error(null, "Email and password do not match"),
                HttpStatus.UNAUTHORIZED);
    }

//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<ApiResponse<Object>> handleException(Exception exception) {
//        return new ResponseEntity<>(ApiResponse.Error(null, exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
