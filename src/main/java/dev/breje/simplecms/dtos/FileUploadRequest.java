package dev.breje.simplecms.dtos;

import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(
        MultipartFile file
) {
}
