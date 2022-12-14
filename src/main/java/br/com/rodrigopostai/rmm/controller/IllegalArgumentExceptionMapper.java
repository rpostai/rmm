package br.com.rodrigopostai.rmm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class IllegalArgumentExceptionMapper extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleException(IllegalArgumentException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
