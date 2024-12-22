package dev.breje.simplecms.controller;

import dev.breje.simplecms.dtos.FileUploadRequest;
import dev.breje.simplecms.dtos.FileUploadResponse;
import dev.breje.simplecms.service.ContentModerationService;
import dev.breje.simplecms.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/v1/file")
public class ContentModerationController {

    private final FileStorageService fileService;
    private final ContentModerationService service;

    @Autowired
    public ContentModerationController(FileStorageService fileService, ContentModerationService service) {
        this.fileService = fileService;
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileUploadResponse> upload(@Validated @RequestParam("file") MultipartFile file) {
        String uploadId = fileService.store(getFileUploadRequest(file));

        service.moderateFile("file path or something");

        return ResponseEntity.ok(new FileUploadResponse(uploadId));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<String> download(@Validated @PathVariable String id) {
        return ResponseEntity.ok("file path or something");
    }

    private FileUploadRequest getFileUploadRequest(MultipartFile file) {
        return new FileUploadRequest(file);
    }

}
