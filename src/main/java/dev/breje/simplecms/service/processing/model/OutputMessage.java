package dev.breje.simplecms.service.processing.model;

public record OutputMessage(
        String userId,
        Integer totalMessages,
        Float averageScore
) {
}
