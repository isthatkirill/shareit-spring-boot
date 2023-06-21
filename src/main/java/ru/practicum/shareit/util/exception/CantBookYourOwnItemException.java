package ru.practicum.shareit.util.exception;

public class CantBookYourOwnItemException extends RuntimeException {

    public CantBookYourOwnItemException(String message) {
        super(message);
    }
}
