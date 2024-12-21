package dev.breje.simplecms.service.translation;

import dev.breje.simplecms.service.translation.dtos.TranslationRequest;
import dev.breje.simplecms.service.translation.dtos.TranslationResponse;
import dev.breje.simplecms.service.translation.exceptions.TranslationServiceConnectionException;
import dev.breje.simplecms.service.translation.exceptions.TranslationServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TranslationService {

    private static final Logger log = LoggerFactory.getLogger(TranslationService.class);
    private final TranslationServiceConnection connection;

    @Autowired
    public TranslationService(TranslationServiceConnection connection) {
        this.connection = connection;
    }

    public String getTranslatedMessage(String message) throws TranslationServiceException {
        TranslationRequest request = createRequest(message);
        TranslationResponse response;
        try {
            log.debug("Translating message...");
            response = connection.translate(request);
        } catch (TranslationServiceConnectionException e) {
            throw new TranslationServiceException("Something went wrong with translation service.", e);
        }
        return response.translatedMessage();
    }

    private TranslationRequest createRequest(String message) {
        return new TranslationRequest(message);
    }
}
