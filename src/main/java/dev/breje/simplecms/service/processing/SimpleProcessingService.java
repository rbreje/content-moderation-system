package dev.breje.simplecms.service.processing;

import dev.breje.simplecms.dtos.FileDownloadRequest;
import dev.breje.simplecms.repository.model.FileEntry;
import dev.breje.simplecms.repository.processing.FileEntryRepository;
import dev.breje.simplecms.service.processing.exceptions.FileNotFoundException;
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
    public boolean isProcessed(FileDownloadRequest request) throws FileNotFoundException {
        Optional<FileEntry> fileEntryOptional = fileEntryRepository.findByUuid(request.id());
        if (fileEntryOptional.isEmpty()) {
            throw new FileNotFoundException("The id couldn't be found.");
        }
        return ProcessingStatus.from(fileEntryOptional.get().getStatus()).isDone();
    }

    // TODO move into a dedicated mapper
    private FileEntry toEntity(FileDownloadRequest request) {
        FileEntry fileEntry = new FileEntry();
        fileEntry.setUuid(request.id());
        fileEntry.setStatus(ProcessingStatus.NEW.getStatus());
        return fileEntry;
    }
}
