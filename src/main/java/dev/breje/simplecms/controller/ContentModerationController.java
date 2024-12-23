package dev.breje.simplecms.controller;

import dev.breje.simplecms.dtos.FileDownloadRequest;
import dev.breje.simplecms.dtos.FileDownloadResponse;
import dev.breje.simplecms.dtos.FileUploadRequest;
import dev.breje.simplecms.dtos.FileUploadResponse;
import dev.breje.simplecms.service.processing.ProcessingService;
import dev.breje.simplecms.service.processing.exceptions.FileNotFoundException;
import dev.breje.simplecms.service.storage.exceptions.StorageException;
import dev.breje.simplecms.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/v1/file")
public class ContentModerationController {

    private final StorageService storageService;
    private final ProcessingService processingService;

    @Autowired
    public ContentModerationController(StorageService storageService, ProcessingService processingService) {
        this.storageService = storageService;
        this.processingService = processingService;
    }

    @PostMapping
    public ResponseEntity<FileUploadResponse> upload(@Validated @RequestParam("file") MultipartFile file) throws StorageException {
        FileUploadResponse response = storageService.store(getFileUploadRequest(file));
        processingService.addFileEntry(getFileDownloadRequest(response.id()));
        return ResponseEntity.ok(response);
        // TODO change status code to 201
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<FileDownloadResponse> download(@Validated @PathVariable String id) throws FileNotFoundException {
        FileDownloadRequest request = getFileDownloadRequest(id);
        if (processingService.isProcessed(request)) {
            // TODO test it
            return ResponseEntity.ok(processingService.download(request));
        }
        // TODO handle the case when something went wrong
        return ResponseEntity.ok(new FileDownloadResponse(id, "IN_PROGRESS", null));
    }

    private FileUploadRequest getFileUploadRequest(MultipartFile file) {
        return new FileUploadRequest(file);
    }

    private FileDownloadRequest getFileDownloadRequest(String id) {
        return new FileDownloadRequest(id);
    }

}
