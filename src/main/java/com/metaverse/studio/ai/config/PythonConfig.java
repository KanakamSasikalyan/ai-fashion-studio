package com.metaverse.studio.ai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Configuration
public class PythonConfig {

    @Bean
    public String pythonScriptPath() throws IOException {
        // Try to locate the script in the classpath first
        ClassPathResource resource = new ClassPathResource("python/generate_clothing.py");

        // If running from JAR, extract to temp directory
        if (!resource.exists() || resource.getURL().toString().contains("jar:")) {
            Path tempScript = Files.createTempFile("generate_clothing_", ".py");
            try (var in = resource.getInputStream()) {
                Files.copy(in, tempScript, StandardCopyOption.REPLACE_EXISTING);
            }
            return tempScript.toAbsolutePath().toString();
        }

        // If running in development (filesystem)
        return resource.getFile().getAbsolutePath();
    }
}