package com.project.trackingservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientException.class)
    public String handleClientException(ClientException ex) {
        log.error("ClientException occurred: {}", ex.getMessage());
        return "An error occurredin client side: " + ex.getMessage();
    }
}
