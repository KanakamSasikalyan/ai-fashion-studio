package com.metaverse.studio.ai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class PythonConfig {

    @Bean
    public String pythonScriptPath() throws IOException {
        return new ClassPathResource("python/generate_clothing.py")
                .getFile().getAbsolutePath();
    }
}