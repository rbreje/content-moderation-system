package dev.breje.simplecms.service.translation.connection;

import dev.breje.simplecms.service.translation.dtos.TranslationRequest;
import dev.breje.simplecms.service.translation.dtos.TranslationResponse;
import dev.breje.simplecms.service.translation.exceptions.TranslationServiceConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

import static java.lang.Thread.sleep;

@Component
public class TranslationServiceConnection {

    private static final Logger log = LoggerFactory.getLogger(TranslationServiceConnection.class);
    private final Random random;

    @Autowired
    public TranslationServiceConnection(Random random) {
        this.random = random;
    }

    public TranslationResponse translate(TranslationRequest request) throws TranslationServiceConnectionException {
        try {
            log.debug("Making request to /translation-service/api/v1/translate endpoint...");
            int fakeLatency = getFakeLatency();
            sleep(fakeLatency);
            log.debug("Response received.");
            log.info("Connection to translation service had a {}ms latency...", fakeLatency);
        } catch (InterruptedException e) {
            throw new TranslationServiceConnectionException("Connection error occurred. ", e);
        }
        return new TranslationResponse(request.originalMessage(), request.originalMessage() + " translated");
    }

    private int getFakeLatency() {
        return 50 + random.nextInt(151);
    }

}
