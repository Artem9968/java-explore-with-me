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
import ru.practicum.mainservice.dto.ApiError;
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
    public ApiError onHandlerMethodValidationException(BadRequestException e) {
        log.error("400 {}.", e.getMessage());
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setReason("Запрос составлен некорректно.");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFoundObject(NotFoundException exception) {
        log.error("404 {}.", exception.getMessage());
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.NOT_FOUND);
        apiError.setReason("Запрошенный объект не найден.");
        apiError.setMessage(exception.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError onValidationException(ValidationException exception) {
        log.error("409 {}.", exception.getMessage());
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.FORBIDDEN);
        apiError.setReason("Запрос содержит недопустимые данные.");
        apiError.setMessage(exception.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError onInternalException(final InternalServerException e) {
        log.error("500 {}", e.getMessage());
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setReason("Внутренняя ошибка сервера.");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

    @ExceptionHandler(DataConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError onDataIntegrityViolationException(final DataConflictException e) {
        log.error("409 {}", e.getMessage());
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.CONFLICT);
        apiError.setReason("Конфликт данных.");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError onDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.error("409 {}", e.getMessage());
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.CONFLICT);
        apiError.setReason(e.getRootCause().getMessage());
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("400 {}.", e.getMessage());
        final List<String> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("Field: %s. Error: %s. Value: '%s'. ",
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .toList();
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setReason("Запрос сформирован некорректно.");
        apiError.setMessage(String.join(" ", violations));
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onConstraintValidationException(ConstraintViolationException e) {
        log.error("400 {}.", e.getMessage());
        final List<String> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> String.format("Field: %s. Error: %s. Value: '%s'. ",
                                violation.getPropertyPath().toString(),
                                violation.getMessage(),
                                violation.getInvalidValue()
                        ))
                .toList();
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setReason("Запрос сформирован некорректно.");
        apiError.setMessage(String.join(" ", violations));
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("400 {}.", e.getMessage());
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setReason("Incorrectly made request.");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(final Exception e) {
        log.error("500 INTERNAL_SERVER_ERROR", e);
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setReason(e.getCause().getMessage());
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

}
