package dev.breje.simplecms.service.translation.connection;

import dev.breje.simplecms.service.translation.dtos.TranslationRequest;
import dev.breje.simplecms.service.translation.dtos.TranslationResponse;
import dev.breje.simplecms.service.translation.exceptions.TranslationServiceConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TranslationServiceConnectionTest {

    private TranslationServiceConnection translationServiceConnection;
    private Random random;

    @BeforeEach
    void setUp() {
        random = mock(Random.class);
        translationServiceConnection = new TranslationServiceConnection(random);
    }

    @Test
    void translate_whenValidRequest_thenReturnTranslatedResponse() throws TranslationServiceConnectionException {
        TranslationRequest request = new TranslationRequest("Hello");
        when(random.nextInt(151)).thenReturn(100);

        TranslationResponse response = translationServiceConnection.translate(request);

        assertEquals("Hello", response.originalMessage());
        assertEquals("Hello translated", response.translatedMessage());
    }

    @Test
    void translate_whenInterruptedException_thenThrowTranslationServiceConnectionException() {
        TranslationRequest request = new TranslationRequest("Hello");
        Thread.currentThread().interrupt();

        TranslationServiceConnectionException exception = assertThrows(TranslationServiceConnectionException.class, () -> {
            translationServiceConnection.translate(request);
        });

        assertTrue(exception.getMessage().contains("Connection error occurred."));
    }
}