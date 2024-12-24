package dev.breje.simplecms.service.processing;

import dev.breje.simplecms.dtos.FileDownloadRequest;
import dev.breje.simplecms.repository.model.FileEntry;
import dev.breje.simplecms.repository.processing.FileEntryRepository;
import dev.breje.simplecms.service.processing.exceptions.FileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SimpleProcessingServiceTest {

    private SimpleProcessingService simpleProcessingService;
    private FileEntryRepository fileEntryRepository;

    @BeforeEach
    void setUp() {
        fileEntryRepository = mock(FileEntryRepository.class);
        simpleProcessingService = new SimpleProcessingService(fileEntryRepository);
    }

    @Test
    void addFileEntry_whenValidRequest_thenFileEntrySaved() {
        FileDownloadRequest request = new FileDownloadRequest("test-id");
        FileEntry fileEntry = new FileEntry();
        fileEntry.setUuid("test-id");
        fileEntry.setStatus(ProcessingStatus.NEW.getStatus());

        simpleProcessingService.addFileEntry(request);

        verify(fileEntryRepository, times(1)).save(fileEntry);
    }

    @Test
    void isProcessed_whenFileExistsAndProcessed_thenReturnTrue() throws FileNotFoundException {
        FileDownloadRequest request = new FileDownloadRequest("test-id");
        FileEntry fileEntry = new FileEntry();
        fileEntry.setUuid("test-id");
        fileEntry.setStatus(ProcessingStatus.DONE.getStatus());
        when(fileEntryRepository.findByUuid("test-id")).thenReturn(Optional.of(fileEntry));

        boolean result = simpleProcessingService.isProcessed(request);

        assertTrue(result);
    }

    @Test
    void isProcessed_whenFileExistsAndNotProcessed_thenReturnFalse() throws FileNotFoundException {
        FileDownloadRequest request = new FileDownloadRequest("test-id");
        FileEntry fileEntry = new FileEntry();
        fileEntry.setUuid("test-id");
        fileEntry.setStatus(ProcessingStatus.IN_PROGRESS.getStatus());
        when(fileEntryRepository.findByUuid("test-id")).thenReturn(Optional.of(fileEntry));

        boolean result = simpleProcessingService.isProcessed(request);

        assertFalse(result);
    }

    @Test
    void isProcessed_whenFileDoesNotExist_thenThrowFileNotFoundException() {
        FileDownloadRequest request = new FileDownloadRequest("non-existent-id");
        when(fileEntryRepository.findByUuid("non-existent-id")).thenReturn(Optional.empty());

        FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> {
            simpleProcessingService.isProcessed(request);
        });

        assertTrue(exception.getMessage().contains("The id couldn't be found."));
    }
}