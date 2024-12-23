package dev.breje.simplecms;

import dev.breje.simplecms.service.processing.exceptions.FileNotFoundException;
import dev.breje.simplecms.service.processing.exceptions.InvalidProcessingStatusException;
import dev.breje.simplecms.service.storage.exceptions.StorageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ContentModerationSystemExceptionHandler {

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<Object> handleStorageException(StorageException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        // TODO replace with good HTTP status code
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidProcessingStatusException.class)
    public ResponseEntity<Object> handleInvalidProcessingStatusException(InvalidProcessingStatusException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        // TODO replace with good HTTP status code
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Object> handleFileNotFoundException(FileNotFoundException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

}
