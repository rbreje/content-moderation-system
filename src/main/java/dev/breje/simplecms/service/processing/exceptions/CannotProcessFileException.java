package dev.breje.simplecms.service.processing.exceptions;

public class CannotProcessFileException extends RuntimeException {

    public CannotProcessFileException(String message) {
        super(message);
    }

    public CannotProcessFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
