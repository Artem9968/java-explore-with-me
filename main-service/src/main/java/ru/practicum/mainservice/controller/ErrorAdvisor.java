package ru.practicum.mainservice.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.mainservice.dto.ErrorResponse;
import ru.practicum.mainservice.exception.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс обработки исключений при обработке поступивших http запросов
 */
@Slf4j
@RestControllerAdvice
public class ErrorAdvisor {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onHandlerMethodValidationException(BadRequestException e) {
        log.error("400 {}.", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setErrorType("Запрос составлен некорректно.");
        errorResponse.setMessage(e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundObject(NotFoundException exception) {
        log.error("404 {}.", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        errorResponse.setErrorType("Запрошенный объект не найден.");
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse onValidationException(ValidationException exception) {
        log.error("409 {}.", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.FORBIDDEN);
        errorResponse.setErrorType("Запрос содержит недопустимые данные.");
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse onInternalException(final InternalServerException e) {
        log.error("500 {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        errorResponse.setErrorType("Внутренняя ошибка сервера.");
        errorResponse.setMessage(e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }

    @ExceptionHandler(DataConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse onDataIntegrityViolationException(final DataConflictException e) {
        log.error("409 {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.CONFLICT);
        errorResponse.setErrorType("Конфликт данных.");
        errorResponse.setMessage(e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse onDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.error("409 {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.CONFLICT);
        errorResponse.setErrorType(e.getRootCause().getMessage());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("400 {}.", e.getMessage());
        final List<String> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("Field: %s. Error: %s. Value: '%s'. ",
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .toList();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setErrorType("Запрос сформирован некорректно.");
        errorResponse.setMessage(String.join(" ", violations));
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        log.error("400 {}.", e.getMessage());
        final List<String> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> String.format("Field: %s. Error: %s. Value: '%s'. ",
                                violation.getPropertyPath().toString(),
                                violation.getMessage(),
                                violation.getInvalidValue()
                        ))
                .toList();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setErrorType("Запрос сформирован некорректно.");
        errorResponse.setMessage(String.join(" ", violations));
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("400 {}.", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setErrorType("Incorrectly made request.");
        errorResponse.setMessage(e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception e) {
        log.error("500 INTERNAL_SERVER_ERROR", e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        errorResponse.setErrorType(e.getCause().getMessage());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }

}
