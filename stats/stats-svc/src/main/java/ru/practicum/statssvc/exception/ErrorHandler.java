package ru.practicum.statssvc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.statsdto.ErrorMessage;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onValidationException(ValidationException exception) {
        log.error("400 {}.", exception.getMessage());
        return new ErrorMessage(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage onInternalException(final InternalServerException e) {
        log.error("500 {}", e.getMessage());
        return new ErrorMessage(e.getMessage());
    }
}
