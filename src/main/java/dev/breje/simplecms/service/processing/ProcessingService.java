package dev.breje.simplecms.service.processing;

import dev.breje.simplecms.dtos.FileDownloadRequest;
import dev.breje.simplecms.dtos.FileDownloadResponse;
import dev.breje.simplecms.dtos.FileUploadRequest;
import dev.breje.simplecms.service.processing.exceptions.FileNotFoundException;
import dev.breje.simplecms.service.processing.exceptions.ProcessingException;

public interface ProcessingService {

    void addFileEntry(FileDownloadRequest request);
    
    boolean isProcessed(FileDownloadRequest request) throws FileNotFoundException;

    FileDownloadResponse download(FileDownloadRequest request);
}
