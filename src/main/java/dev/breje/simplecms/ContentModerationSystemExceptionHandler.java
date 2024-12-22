package dev.breje.simplecms;

import dev.breje.simplecms.service.storage.StorageException;
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

}
