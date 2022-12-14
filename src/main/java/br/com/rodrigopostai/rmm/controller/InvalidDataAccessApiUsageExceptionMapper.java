package br.com.rodrigopostai.rmm.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class InvalidDataAccessApiUsageExceptionMapper extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    protected ResponseEntity<Object> handleException(InvalidDataAccessApiUsageException e) {
        return ResponseEntity.internalServerError().body(e.getCause().getMessage());
    }
}
