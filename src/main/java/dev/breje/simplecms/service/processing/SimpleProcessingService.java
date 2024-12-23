package dev.breje.simplecms.service.processing;

import dev.breje.simplecms.dtos.FileDownloadRequest;
import dev.breje.simplecms.dtos.FileDownloadResponse;
import dev.breje.simplecms.repository.model.FileEntry;
import dev.breje.simplecms.repository.processing.FileEntryRepository;
import dev.breje.simplecms.service.processing.exceptions.FileNotFoundException;
import dev.breje.simplecms.service.processing.exceptions.InvalidProcessingStatusException;
import dev.breje.simplecms.service.processing.exceptions.ProcessingException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SimpleProcessingService implements ProcessingService {

    private final FileEntryRepository fileEntryRepository;

    public SimpleProcessingService(FileEntryRepository fileEntryRepository) {
        this.fileEntryRepository = fileEntryRepository;
    }

    @Override
    public void addFileEntry(FileDownloadRequest request) {
        fileEntryRepository.save(toEntity(request));
    }

    @Override
    public boolean isProcessed(FileDownloadRequest request) throws ProcessingException, FileNotFoundException {
        Optional<FileEntry> fileEntryOptional = fileEntryRepository.findByUuid(request.id());
        if (fileEntryOptional.isEmpty()) {
            throw new FileNotFoundException("The id couldn't be found.");
        }
        FileEntry fileEntry = fileEntryOptional.get();
        try {
            return ProcessingStatus.from(fileEntry.getStatus()).isDone();
        } catch (InvalidProcessingStatusException e) {
            throw new ProcessingException("Something went wrong retrieving the file status.", e);
        }
    }

    @Override
    public FileDownloadResponse download(FileDownloadRequest request) {
        return null;
    }

    // TODO move into a dedicated mapper
    private FileEntry toEntity(FileDownloadRequest request) {
        FileEntry fileEntry = new FileEntry();
        fileEntry.setUuid(request.id());
        fileEntry.setStatus(ProcessingStatus.NEW.getStatus());
        return fileEntry;
    }
}
