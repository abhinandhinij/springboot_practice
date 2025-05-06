package com.practice.spring.error;

import com.practice.spring.entity.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserErrorHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<UserResponse> handleUserException(UsernameNotFoundException exception)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserResponse(exception.getMessage()));
    }
    
}
