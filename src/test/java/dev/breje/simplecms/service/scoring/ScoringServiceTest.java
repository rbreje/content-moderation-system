package dev.breje.simplecms.service.scoring;

import dev.breje.simplecms.service.scoring.connection.ScoringServiceConnection;
import dev.breje.simplecms.service.scoring.dtos.ScoringRequest;
import dev.breje.simplecms.service.scoring.dtos.ScoringResponse;
import dev.breje.simplecms.service.scoring.exceptions.ScoringServiceConnectionException;
import dev.breje.simplecms.service.scoring.exceptions.ScoringServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScoringServiceTest {

    private ScoringService scoringService;
    private ScoringServiceConnection connection;

    @BeforeEach
    void setUp() {
        connection = mock(ScoringServiceConnection.class);
        scoringService = new ScoringService(connection);
    }

    @Test
    void getScore_whenHappyFlow_thenReturnsValidScore() throws ScoringServiceConnectionException, ScoringServiceException {
        String message = "Test message";
        ScoringRequest request = new ScoringRequest(message);
        ScoringResponse response = new ScoringResponse(message, 0.75f);
        when(connection.score(request)).thenReturn(response);

        float score = scoringService.getScore(message);

        assertEquals(0.75f, score);
    }

    @Test
    void getScore_whenError_thenHandlesConnectionException() throws ScoringServiceConnectionException {
        String message = "Test message";
        ScoringRequest request = new ScoringRequest(message);
        when(connection.score(request)).thenThrow(new ScoringServiceConnectionException("Connection error", new Exception()));

        ScoringServiceException exception = assertThrows(ScoringServiceException.class, () -> {
            scoringService.getScore(message);
        });

        assertTrue(exception.getMessage().contains("Something went wrong with scoring service."));
    }
}