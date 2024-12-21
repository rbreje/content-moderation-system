package dev.breje.simplecms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Random;

@Configuration
public class ContentModerationSystemConfig {

    @Bean
    @Scope("singleton")
    public Random random() {
        return new Random();
    }
    
    
}
