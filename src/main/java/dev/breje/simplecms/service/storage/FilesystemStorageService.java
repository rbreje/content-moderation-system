package dev.breje.simplecms.service.storage;

import dev.breje.simplecms.dtos.FileDownloadRequest;
import dev.breje.simplecms.dtos.FileUploadRequest;
import dev.breje.simplecms.dtos.FileUploadResponse;
import dev.breje.simplecms.service.storage.exceptions.CannotWriteFileException;
import dev.breje.simplecms.service.storage.exceptions.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

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
    public FileUploadResponse store(FileUploadRequest request) throws StorageException {
        String uuid = UUID.randomUUID().toString();
        String filename = uuid + ".csv";
        Path destination = this.rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();
        try (InputStream inputStream = request.file().getInputStream()) {
            Files.copy(inputStream, destination,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
        return new FileUploadResponse(uuid);
    }

    @Override
    public void init() throws StorageException {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage.", e);
        }
    }

    @Override
    public void clear() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public Path getFilePath(String id) {
        String filename = id + ".csv";
        return this.rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();
    }

    @Override
    public void storeContent(String uuid, String content) throws StorageException {
        String filename = uuid + "-output.csv";
        try {
            Path destination = this.rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();
            Files.write(destination, content.getBytes());
        } catch (IOException e) {
            throw new CannotWriteFileException("Couldn't write the output file.", e);
        }
    }

    @Override
    public Resource download(FileDownloadRequest request) throws StorageException {
        String filename = request.id() + "-output.csv";
        try {
            Path source = this.rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();
            return new ByteArrayResource(Files.readAllBytes(source));
        } catch (IOException e) {
            throw new StorageException("Couldn't load the output file.", e);
        }
    }
}
