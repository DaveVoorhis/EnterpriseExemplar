package org.reldb.exemplars.java.backend.exception.controllers;

import org.reldb.exemplars.java.backend.exception.ErrorResponse;
import org.reldb.exemplars.java.backend.exception.custom.CustomExceptionBase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.slf4j.event.Level;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class CustomExceptionController {
    @ExceptionHandler(value = {CustomExceptionBase.class})
    @ResponseBody
    public ResponseEntity<@NonNull ErrorResponse> handleCustomException(CustomExceptionBase customException) {
        log
                .atLevel(customException.getLogLevel())
                .setCause(customException.getLogLevel() == Level.ERROR ? customException : null)
                .setMessage(customException.getMessage())
                .log();
        return new ResponseEntity<>(customException.getResponse(), customException.getStatusCode());
    }
}
