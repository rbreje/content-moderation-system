package dev.breje.simplecms.service;

import dev.breje.simplecms.dtos.FileUploadRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FileStorageService {
    public String store(FileUploadRequest request) {
        return UUID.randomUUID().toString();
    }
}
