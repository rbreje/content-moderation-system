package dev.breje.simplecms.dtos;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public record FileDownloadResponse(
        String id,
        String status,
        Resource file
) {
}
