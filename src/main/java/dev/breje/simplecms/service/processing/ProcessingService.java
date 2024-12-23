package dev.breje.simplecms.service.processing;

import dev.breje.simplecms.dtos.FileDownloadRequest;
import dev.breje.simplecms.service.processing.exceptions.FileNotFoundException;

public interface ProcessingService {

    void addFileEntry(FileDownloadRequest request);

    boolean isProcessed(FileDownloadRequest request) throws FileNotFoundException;
}
