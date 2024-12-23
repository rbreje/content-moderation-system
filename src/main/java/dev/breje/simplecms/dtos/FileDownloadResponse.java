package dev.breje.simplecms.dtos;

import org.springframework.web.multipart.MultipartFile;

public record FileDownloadResponse(
        String id,
        String status,
        MultipartFile file
) {
}
