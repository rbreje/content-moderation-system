package dev.breje.simplecms.service.scoring.connection;

import dev.breje.simplecms.service.scoring.dtos.ScoringRequest;
import dev.breje.simplecms.service.scoring.dtos.ScoringResponse;
import dev.breje.simplecms.service.scoring.exceptions.ScoringServiceConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScoringServiceConnectionTest {

    private ScoringServiceConnection scoringServiceConnection;
    private Random random;

    @BeforeEach
    void setUp() {
        random = mock(Random.class);
        scoringServiceConnection = new ScoringServiceConnection(random);
    }

    @Test
    void scoreReturnsValidResponse() throws ScoringServiceConnectionException {
        ScoringRequest request = new ScoringRequest("Test message");
        when(random.nextInt(151)).thenReturn(100);
        when(random.nextFloat()).thenReturn(0.75f);

        ScoringResponse response = scoringServiceConnection.score(request);

        assertEquals("Test message", response.message());
        assertEquals(0.75f, response.score());
    }

    @Test
    void scoreHandlesInterruptedException() {
        ScoringRequest request = new ScoringRequest("Test message");
        Thread.currentThread().interrupt(); // Simulate InterruptedException

        ScoringServiceConnectionException exception = assertThrows(ScoringServiceConnectionException.class, () -> {
            scoringServiceConnection.score(request);
        });

        assertTrue(exception.getMessage().contains("Connection error occurred."));
    }
}