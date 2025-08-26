package ru.practicum.mainservice.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.mainservice.dto.error.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private ErrorResponse buildApiError(HttpStatus status, String reason, String message) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(status);
        error.setReason(reason);
        error.setMessage(message);
        error.setTimestamp(LocalDateTime.now());
        return error;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(BadRequestException e) {
        log.error("400 BAD_REQUEST: {}", e.getMessage());
        return buildApiError(HttpStatus.BAD_REQUEST, "Запрос составлен некорректно.", e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException e) {
        log.error("404 NOT_FOUND: {}", e.getMessage());
        return buildApiError(HttpStatus.NOT_FOUND, "Запрошенный объект не найден.", e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleValidation(ValidationException e) {
        log.error("403 FORBIDDEN: {}", e.getMessage());
        return buildApiError(HttpStatus.FORBIDDEN, "Запрос содержит недопустимые данные.", e.getMessage());
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternal(InternalServerErrorException e) {
        log.error("500 INTERNAL_SERVER_ERROR: {}", e.getMessage());
        return buildApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера.", e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataConflict(ConflictException e) {
        log.error("409 CONFLICT: {}", e.getMessage());
        return buildApiError(HttpStatus.CONFLICT, "Конфликт данных.", e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrity(DataIntegrityViolationException e) {
        log.error("409 CONFLICT: {}", e.getMessage());
        String reason = (e.getRootCause() != null) ? e.getRootCause().getMessage() : "Нарушение целостности данных";
        return buildApiError(HttpStatus.CONFLICT, reason, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgNotValid(MethodArgumentNotValidException e) {
        log.error("400 BAD_REQUEST: {}", e.getMessage());
        List<String> violations = e.getBindingResult().getFieldErrors().stream()
                .map(err -> String.format("Field: %s. Error: %s. Value: '%s'.",
                        err.getField(), err.getDefaultMessage(), err.getRejectedValue()))
                .toList();
        return buildApiError(HttpStatus.BAD_REQUEST, "Запрос сформирован некорректно.", String.join(" ", violations));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException e) {
        log.error("400 BAD_REQUEST: {}", e.getMessage());
        List<String> violations = e.getConstraintViolations().stream()
                .map(v -> String.format("Field: %s. Error: %s. Value: '%s'.",
                        v.getPropertyPath(), v.getMessage(), v.getInvalidValue()))
                .toList();
        return buildApiError(HttpStatus.BAD_REQUEST, "Запрос сформирован некорректно.", String.join(" ", violations));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.error("400 BAD_REQUEST: {}", e.getMessage());
        return buildApiError(HttpStatus.BAD_REQUEST, "Некорректно составлен запрос.", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(Exception e) {
        log.error("500 INTERNAL_SERVER_ERROR", e);
        String reason = (e.getCause() != null) ? e.getCause().getMessage() : "Неизвестная ошибка";
        return buildApiError(HttpStatus.INTERNAL_SERVER_ERROR, reason, e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingParams(MissingServletRequestParameterException e) {
        log.error("400 Ошибка: отсутствует обязательный параметр {}.", e.getParameterName());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setReason("Отсутствует обязательный параметр запроса");
        errorResponse.setMessage("Не указан параметр: " + e.getParameterName());
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }
}

