package dev.breje.simplecms.service.storage;

import dev.breje.simplecms.dtos.FileDownloadRequest;
import dev.breje.simplecms.dtos.FileUploadRequest;
import dev.breje.simplecms.dtos.FileUploadResponse;
import dev.breje.simplecms.service.storage.exceptions.StorageException;
import org.springframework.core.io.Resource;

import java.nio.file.Path;

public interface StorageService {

    void init() throws StorageException;

    FileUploadResponse store(FileUploadRequest request) throws StorageException;

    void clear();

    Path getFilePath(String id);

    void storeContent(String id, String content) throws StorageException;
    
    Resource download(FileDownloadRequest request) throws StorageException;
}
