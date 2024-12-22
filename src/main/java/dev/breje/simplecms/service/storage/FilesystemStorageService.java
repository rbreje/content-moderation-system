package dev.breje.simplecms.service.storage;

import dev.breje.simplecms.dtos.FileUploadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FilesystemStorageService implements StorageService {

    private final Path rootLocation;
    // TODO create mapper from dto to entity

    @Autowired
    public FilesystemStorageService(StorageProperties properties) {
        // TODO validate the root location
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public String store(FileUploadRequest request) throws StorageException {
        String uuid = UUID.randomUUID().toString();
        String filename = uuid + ".csv";
        Path destination = this.rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();
        try (InputStream inputStream = request.file().getInputStream()) {
            Files.copy(inputStream, destination,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
        return uuid;
    }

    @Override
    public void init() throws StorageException {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage.", e);
        }
    }
}
