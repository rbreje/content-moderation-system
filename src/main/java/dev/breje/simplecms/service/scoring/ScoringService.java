package dev.breje.simplecms.service.scoring;

import dev.breje.simplecms.service.scoring.connection.ScoringServiceConnection;
import dev.breje.simplecms.service.scoring.dtos.ScoringRequest;
import dev.breje.simplecms.service.scoring.dtos.ScoringResponse;
import dev.breje.simplecms.service.scoring.exceptions.ScoringServiceConnectionException;
import dev.breje.simplecms.service.scoring.exceptions.ScoringServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoringService {

    private static final Logger log = LoggerFactory.getLogger(ScoringService.class);
    private final ScoringServiceConnection connection;

    @Autowired
    public ScoringService(ScoringServiceConnection connection) {
        this.connection = connection;
    }

    public float getScore(String message) throws ScoringServiceException {
        ScoringRequest request = createRequest(message);
        ScoringResponse response;
        try {
            log.debug("Scoring message...");
            response = connection.score(request);
        } catch (ScoringServiceConnectionException e) {
            throw new ScoringServiceException("Something went wrong with scoring service.", e);
        }
        return response.score();
    }

    private ScoringRequest createRequest(String message) {
        return new ScoringRequest(message);
    }
}
