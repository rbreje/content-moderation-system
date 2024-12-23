package dev.breje.simplecms;

import dev.breje.simplecms.service.processing.CsvProcessor;
import dev.breje.simplecms.service.storage.StorageProperties;
import dev.breje.simplecms.service.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class ContentModerationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentModerationSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService, CsvProcessor csvProcessor) {
        return args -> {
            storageService.clear();
            storageService.init();
            csvProcessor.run();
        };
    }

}
