package dev.breje.simplecms.service.scoring.connection;

import dev.breje.simplecms.service.scoring.dtos.ScoringRequest;
import dev.breje.simplecms.service.scoring.dtos.ScoringResponse;
import dev.breje.simplecms.service.scoring.exceptions.ScoringServiceConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

import static java.lang.Thread.sleep;

@Component
public class ScoringServiceConnection {

    private static final Logger log = LoggerFactory.getLogger(ScoringServiceConnection.class);
    private final Random random;

    @Autowired
    public ScoringServiceConnection(Random random) {
        this.random = random;
    }

    public ScoringResponse score(ScoringRequest request) throws ScoringServiceConnectionException {
        try {
            log.debug("Making request to /scoring-service/api/v1/score endpoint...");
            int fakeLatency = getFakeLatency();
            sleep(fakeLatency);
            log.debug("Response received.");
            log.info("Connection to scoring service had a {}ms latency...", fakeLatency);
        } catch (InterruptedException e) {
            throw new ScoringServiceConnectionException("Connection error occurred. ", e);
        }
        return new ScoringResponse(request.message(), getFakeScore());
    }

    private int getFakeLatency() {
        return 50 + random.nextInt(151);
    }

    private float getFakeScore() {
        return random.nextFloat();
    }
}
