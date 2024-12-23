package dev.breje.simplecms.service.processing.exceptions;

public class CannotProcessUserMessageException extends RuntimeException {

    public CannotProcessUserMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
