package dev.breje.simplecms.service.storage.exceptions;

public class CannotWriteFileException extends StorageException {

    public CannotWriteFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
