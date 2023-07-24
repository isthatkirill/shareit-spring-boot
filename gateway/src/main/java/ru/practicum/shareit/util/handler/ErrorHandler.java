package ru.practicum.shareit.util.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;


@Slf4j
@RestControllerAdvice("ru.practicum.shareit")
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage validationHandle(final MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        log.warn("{}: {}", e.getClass().getSimpleName(), errorMessage);
        return new ErrorMessage("Validation error", errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage missingHeaderHandle(final MissingRequestHeaderException e) {
        log.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErrorMessage("Missing request header", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage missingHeaderHandle(final ConstraintViolationException e) {
        log.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErrorMessage("Validation error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage missingRequestParameterHandle(final MissingServletRequestParameterException e) {
        log.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErrorMessage("Missing request parameter", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage illegalArgumentHandle(final IllegalArgumentException e) {
        log.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErrorMessage("Illegal argument", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage unexpectedErrorHandle(final Throwable e) {
        log.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErrorMessage("Unexpected error occurred", e.getMessage());
    }

}
