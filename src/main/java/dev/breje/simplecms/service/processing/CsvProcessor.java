package dev.breje.simplecms.service.processing;

import dev.breje.simplecms.repository.model.FileEntry;
import dev.breje.simplecms.repository.processing.FileEntryRepository;
import dev.breje.simplecms.service.processing.exceptions.CannotProcessFileException;
import dev.breje.simplecms.service.processing.exceptions.CannotProcessUserMessageException;
import dev.breje.simplecms.service.processing.model.OutputMessage;
import dev.breje.simplecms.service.processing.model.WorkingMessage;
import dev.breje.simplecms.service.scoring.ScoringService;
import dev.breje.simplecms.service.scoring.exceptions.ScoringServiceException;
import dev.breje.simplecms.service.storage.StorageService;
import dev.breje.simplecms.service.storage.exceptions.StorageException;
import dev.breje.simplecms.service.translation.TranslationService;
import dev.breje.simplecms.service.translation.exceptions.TranslationServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

@Service
public class CsvProcessor implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(CsvProcessor.class);

    private final FileEntryRepository fileEntryRepository;
    private final StorageService storageService;
    private final TranslationService translationService;
    private final ScoringService scoringService;
    private final Map<FileEntry, String> workingMap = new ConcurrentHashMap<>();
    private final ExecutorService executorService;

    @Autowired
    public CsvProcessor(FileEntryRepository fileEntryRepository, StorageService storageService, TranslationService translationService, ScoringService scoringService) {
        this.fileEntryRepository = fileEntryRepository;
        this.storageService = storageService;
        this.translationService = translationService;
        this.scoringService = scoringService;

        // TODO make it configurable
        executorService = Executors.newFixedThreadPool(8);
    }

    private void processEntry(FileEntry fileEntry) {
        // moving the current file to IN PROGRESS
        fileEntry.setStatus(ProcessingStatus.IN_PROGRESS.getStatus());
        fileEntryRepository.save(fileEntry);

        // retrieving the file behind the request
        Path path = storageService.getFilePath(fileEntry.getUuid());
        List<WorkingMessage> workingMessages;
        try (BufferedReader br = Files.newBufferedReader(path)) {
            // parse the CSV file and load objects
            workingMessages = br.lines().skip(1).map(line ->
            {
                String[] elements = line.split(",");
                WorkingMessage wm = new WorkingMessage();
                wm.setUserId(elements[0]);
                wm.setOriginalMessage(elements[1]);
                return wm;
            }).toList();
        } catch (IOException e) {
            throw new CannotProcessFileException("An error occurred while processing a file.", e);
        }

        ExecutorService dedicatedExecutorService = Executors.newFixedThreadPool(8);
        // TODO make it configurable

        // process the messages on separate threads
        workingMessages.forEach(message ->
                dedicatedExecutorService.submit(() -> {
                    try {
                        message.setTranslatedMessage(translationService.getTranslatedMessage(message.getOriginalMessage()));
                        message.setScore(scoringService.getScore(message.getTranslatedMessage()));
                    } catch (TranslationServiceException | ScoringServiceException tse) {
                        throw new CannotProcessUserMessageException("Couldn't process an user message.", tse);
                    }
                })
        );
        dedicatedExecutorService.shutdown();
        try {
            // TODO make the timeout configurable
            // FIXME find a better way
            boolean tasksFinished = dedicatedExecutorService.awaitTermination(10, TimeUnit.MINUTES);
            if (!tasksFinished) {
                throw new CannotProcessFileException("Unexpected error occurred when processing messages..");
            }
        } catch (InterruptedException e) {
            throw new CannotProcessFileException("Unexpected error occurred when processing messages..", e);
        }

        // compute the output & dump the outcome to the file
        try {
            storageService.storeContent(fileEntry.getUuid(), getExecutionSummary(getComputedOutput(workingMessages)));
        } catch (StorageException e) {
            throw new CannotProcessFileException("The output file cannot be processed.", e);
        }

        // mark the file as done in DB
        fileEntry.setStatus(ProcessingStatus.DONE.getStatus());
        fileEntryRepository.save(fileEntry);
    }

    private Set<OutputMessage> getComputedOutput(List<WorkingMessage> workingMessages) {
        Map<String, Integer> totalMessagesPerUser = new HashMap<>();
        Map<String, Float> averageScorePerUser = new HashMap<>();
        workingMessages.forEach(message -> {
            totalMessagesPerUser.put(message.getUserId(), totalMessagesPerUser.getOrDefault(message.getUserId(), 0) + 1);
            averageScorePerUser.put(
                    message.getUserId(),
                    averageScorePerUser.getOrDefault(message.getUserId(), 0f) + message.getScore() / totalMessagesPerUser.getOrDefault(message.getUserId(), 1)
            );
        });
        return totalMessagesPerUser
                .keySet()
                .stream()
                .map(userId -> new OutputMessage(userId, totalMessagesPerUser.get(userId), averageScorePerUser.get(userId)))
                .collect(Collectors.toSet());
    }

    private String getExecutionSummary(Set<OutputMessage> outputMessages) {
        StringBuilder sb = new StringBuilder();
        sb.append("user_id,total_messages,avg_score");
        sb.append(System.lineSeparator());
        outputMessages.forEach(message ->
                {
                    sb.append(message.userId());
                    sb.append(",");
                    sb.append(message.totalMessages());
                    sb.append(",");
                    sb.append(message.averageScore());
                    sb.append(System.lineSeparator());
                }
        );
        return sb.toString();
    }

    private void loadData() {
        List<FileEntry> candidates = fileEntryRepository.findByStatus(ProcessingStatus.NEW.getStatus());
        candidates.forEach(fileEntry -> {
            if (!workingMap.containsKey(fileEntry)) {
                workingMap.put(fileEntry, fileEntry.getStatus());
            }
        });
    }

    @Override
    public void run() {
        while (true) {
            try {
                loadData();
                workingMap.entrySet()
                        .stream()
                        .filter(entry -> ProcessingStatus.from(entry.getValue()).isNew())
                        .forEach(entry -> {
                            executorService.submit(
                                    () -> processEntry(entry.getKey())
                            );
                            entry.setValue(ProcessingStatus.IN_PROGRESS.getStatus());
                        });
                sleep(1000);
                // TODO make it configurable
            } catch (InterruptedException e) {
                log.error("Couldn't process the CSV parsing.", e);
            }
        }
    }
}
