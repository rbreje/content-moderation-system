package dev.breje.simplecms.service.processing;

import dev.breje.simplecms.domain.InputMessage;
import dev.breje.simplecms.service.scoring.ScoringService;
import dev.breje.simplecms.service.translation.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CsvProcessor implements Runnable {

    private final TranslationService translationService;
    private final ScoringService scoringService;
    
    private ExecutorService executorService;

    @Autowired
    public CsvProcessor(TranslationService translationService, ScoringService scoringService) {
        this.translationService = translationService;
        this.scoringService = scoringService;
        
        // TODO make it configurable
        executorService = Executors.newFixedThreadPool(8);
    }
    
    public String moderateFile(String localPath) {
        // generate unique id for the file
        // parse the file line by line
        // send each line to processing
        return "123";
    }
    
    public void moderateMessage(InputMessage message) {
        executorService.submit(() -> {
            
        });
    }


    @Override
    public void run() {
        // check DB for every added file
        // launch worker for each line of the CSV file
    }
}
