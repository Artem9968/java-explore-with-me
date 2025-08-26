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
import ru.practicum.mainservice.dto.error.ApiError;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private ApiError buildApiError(HttpStatus status, String reason, String message) {
        ApiError error = new ApiError();
        error.setStatus(status);
        error.setReason(reason);
        error.setMessage(message);
        error.setTimestamp(LocalDateTime.now());
        return error;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(BadRequestException e) {
        log.error("400 BAD_REQUEST: {}", e.getMessage());
        return buildApiError(HttpStatus.BAD_REQUEST, "Запрос составлен некорректно.", e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(NotFoundException e) {
        log.error("404 NOT_FOUND: {}", e.getMessage());
        return buildApiError(HttpStatus.NOT_FOUND, "Запрошенный объект не найден.", e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleValidation(ValidationException e) {
        log.error("403 FORBIDDEN: {}", e.getMessage());
        return buildApiError(HttpStatus.FORBIDDEN, "Запрос содержит недопустимые данные.", e.getMessage());
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInternal(InternalServerErrorException e) {
        log.error("500 INTERNAL_SERVER_ERROR: {}", e.getMessage());
        return buildApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера.", e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataConflict(ConflictException e) {
        log.error("409 CONFLICT: {}", e.getMessage());
        return buildApiError(HttpStatus.CONFLICT, "Конфликт данных.", e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrity(DataIntegrityViolationException e) {
        log.error("409 CONFLICT: {}", e.getMessage());
        String reason = (e.getRootCause() != null) ? e.getRootCause().getMessage() : "Нарушение целостности данных";
        return buildApiError(HttpStatus.CONFLICT, reason, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgNotValid(MethodArgumentNotValidException e) {
        log.error("400 BAD_REQUEST: {}", e.getMessage());
        List<String> violations = e.getBindingResult().getFieldErrors().stream()
                .map(err -> String.format("Field: %s. Error: %s. Value: '%s'.",
                        err.getField(), err.getDefaultMessage(), err.getRejectedValue()))
                .toList();
        return buildApiError(HttpStatus.BAD_REQUEST, "Запрос сформирован некорректно.", String.join(" ", violations));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolation(ConstraintViolationException e) {
        log.error("400 BAD_REQUEST: {}", e.getMessage());
        List<String> violations = e.getConstraintViolations().stream()
                .map(v -> String.format("Field: %s. Error: %s. Value: '%s'.",
                        v.getPropertyPath(), v.getMessage(), v.getInvalidValue()))
                .toList();
        return buildApiError(HttpStatus.BAD_REQUEST, "Запрос сформирован некорректно.", String.join(" ", violations));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.error("400 BAD_REQUEST: {}", e.getMessage());
        return buildApiError(HttpStatus.BAD_REQUEST, "Некорректно составлен запрос.", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleGeneric(Exception e) {
        log.error("500 INTERNAL_SERVER_ERROR", e);
        String reason = (e.getCause() != null) ? e.getCause().getMessage() : "Неизвестная ошибка";
        return buildApiError(HttpStatus.INTERNAL_SERVER_ERROR, reason, e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingParams(MissingServletRequestParameterException e) {
        log.error("400 Ошибка: отсутствует обязательный параметр {}.", e.getParameterName());
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setReason("Отсутствует обязательный параметр запроса");
        apiError.setMessage("Не указан параметр: " + e.getParameterName());
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }
}

