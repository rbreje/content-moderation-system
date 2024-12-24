package dev.breje.simplecms.service.translation;

import dev.breje.simplecms.service.translation.connection.TranslationServiceConnection;
import dev.breje.simplecms.service.translation.dtos.TranslationRequest;
import dev.breje.simplecms.service.translation.dtos.TranslationResponse;
import dev.breje.simplecms.service.translation.exceptions.TranslationServiceConnectionException;
import dev.breje.simplecms.service.translation.exceptions.TranslationServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TranslationServiceTest {

    private TranslationService translationService;
    private TranslationServiceConnection connection;

    @BeforeEach
    void setUp() {
        connection = mock(TranslationServiceConnection.class);
        translationService = new TranslationService(connection);
    }

    @Test
    void getTranslatedMessage_whenValidMessage_thenReturnTranslatedMessage() throws TranslationServiceConnectionException, TranslationServiceException {
        String message = "Hello";
        TranslationRequest request = new TranslationRequest(message);
        TranslationResponse response = new TranslationResponse(message, "Hello translated");
        when(connection.translate(request)).thenReturn(response);

        String translatedMessage = translationService.getTranslatedMessage(message);

        assertEquals("Hello translated", translatedMessage);
    }

    @Test
    void getTranslatedMessage_whenConnectionException_thenThrowTranslationServiceException() throws TranslationServiceConnectionException {
        String message = "Hello";
        TranslationRequest request = new TranslationRequest(message);
        when(connection.translate(request)).thenThrow(new TranslationServiceConnectionException("Connection error", new Exception("test")));

        TranslationServiceException exception = assertThrows(TranslationServiceException.class, () -> {
            translationService.getTranslatedMessage(message);
        });

        assertTrue(exception.getMessage().contains("Something went wrong with translation service."));
    }
}