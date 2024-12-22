package dev.breje.simplecms.domain;

public record WorkingMessage(
        String userId,
        String originalMessage,
        String translatedMessage,
        float score,
        ProcessingStatus status
) {
}
