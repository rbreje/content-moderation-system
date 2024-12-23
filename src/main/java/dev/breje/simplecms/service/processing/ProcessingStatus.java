package dev.breje.simplecms.service.processing;

import dev.breje.simplecms.service.processing.exceptions.InvalidProcessingStatusException;

import java.util.Arrays;

public enum ProcessingStatus {

    NEW("new"),

    IN_PROGRESS("in_progress"),

    DONE("done"),

    ERROR("error");

    private final String status;

    ProcessingStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static ProcessingStatus from(String status) throws InvalidProcessingStatusException {
        return Arrays.stream(values())
                .filter(value -> value.getStatus().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new InvalidProcessingStatusException("Unexpected status provided: " + status));
    }
    
    public boolean isDone() {
        return this == DONE;
    }
}
