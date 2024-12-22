package dev.breje.simplecms.controller;

import dev.breje.simplecms.dtos.FileUploadRequest;
import dev.breje.simplecms.dtos.FileUploadResponse;
import dev.breje.simplecms.service.storage.StorageException;
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

    @Autowired
    public ContentModerationController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    public ResponseEntity<FileUploadResponse> upload(@Validated @RequestParam("file") MultipartFile file) throws StorageException {
        String uploadId = storageService.store(getFileUploadRequest(file));
        return ResponseEntity.ok(new FileUploadResponse(uploadId));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<String> download(@Validated @PathVariable String id) {
        // convert to request
        // return response
        return ResponseEntity.ok("file path or something");
    }

    private FileUploadRequest getFileUploadRequest(MultipartFile file) {
        return new FileUploadRequest(file);
    }

}
