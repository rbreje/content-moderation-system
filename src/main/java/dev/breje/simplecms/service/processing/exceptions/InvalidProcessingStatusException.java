package dev.breje.simplecms.service.processing.exceptions;

public class InvalidProcessingStatusException extends RuntimeException {

    public InvalidProcessingStatusException(String message) {
        super(message);
    }
}
