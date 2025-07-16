package com.clinio.shared.web;

import com.clinio.shared.domain.DomainValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DomainExceptionHandler {

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<String> handle(DomainValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
