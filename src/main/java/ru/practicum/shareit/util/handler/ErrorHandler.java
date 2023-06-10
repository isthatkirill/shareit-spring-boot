package ru.practicum.shareit.util.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.util.exception.IncorrectOwnerException;
import ru.practicum.shareit.util.exception.NotFoundException;
import ru.practicum.shareit.util.exception.UniqueEmailException;

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
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage notUniqueEmailHandle(final UniqueEmailException e) {
        log.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErrorMessage("Validation error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage missingHeaderHandle(final MissingRequestHeaderException e) {
        log.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErrorMessage("Missing request header", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage notFoundHandle(final NotFoundException e) {
        log.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErrorMessage("Entity not found", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage missingHeaderHandle(final IncorrectOwnerException e) {
        log.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErrorMessage("Incorrect owner", e.getMessage());
    }

}
