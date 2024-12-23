package dev.breje.simplecms.repository.processing;

import dev.breje.simplecms.repository.model.FileEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileEntryRepository extends JpaRepository<FileEntry, Long> {

    Optional<FileEntry> findByUuid(String uuid);

}
