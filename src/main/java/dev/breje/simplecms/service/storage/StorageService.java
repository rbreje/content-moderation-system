package dev.breje.simplecms.service.storage;

import dev.breje.simplecms.dtos.FileUploadRequest;

public interface StorageService {

    void init() throws StorageException;

    String store(FileUploadRequest request) throws StorageException;

    void clear();
}
