package dev.breje.simplecms.service.translation.dtos;

public record TranslationResponse(
        String originalMessage,
        String translatedMessage
) {
}
