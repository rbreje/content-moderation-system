package dev.breje.simplecms.service.storage;

import dev.breje.simplecms.dtos.FileUploadRequest;
import dev.breje.simplecms.dtos.FileUploadResponse;

public interface StorageService {

    void init() throws StorageException;

    FileUploadResponse store(FileUploadRequest request) throws StorageException;

    void clear();
}
