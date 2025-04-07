package com.metaverse.studio.ai.service;

import com.metaverse.studio.ai.model.Design;
import com.metaverse.studio.ai.repository.DesignRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AIClothingService {

    Logger log = LoggerFactory.getLogger(AIClothingService.class);

    @Autowired
    DesignRepository designRepository;

    @Value("${ai.output.dir}")
    private String outputDir;

    private final String pythonScriptPath;

    public AIClothingService(String pythonScriptPath) {
        this.pythonScriptPath = pythonScriptPath;
    }

    public byte[] generateClothingDesign(String prompt, String style) throws IOException, IOException {
        // Ensure output directory exists
        Files.createDirectories(Paths.get(outputDir));

        ProcessBuilder pb = new ProcessBuilder(
                "python",
                pythonScriptPath,
                "\"" + prompt + "\"",
                style,
                outputDir
        );

        // Redirect error stream to standard output
        pb.redirectErrorStream(true);

        Process process = pb.start();

        // Capture and log Python output in real-time
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ERROR")) {
                    log.error("Python error: {}", line);
                    throw new IOException("AI generation failed: " + line);
                } else if (!line.trim().isEmpty()) {
                    try {
                        // Clean the line before decoding
                        log.info("Python: " + line);
                        String cleanLine = line.trim()
                                .replaceAll("[^A-Za-z0-9+/=]", "");
                        byte[] imageBytes = Base64.getDecoder().decode(cleanLine);

                        Design design = createDesignEntity(prompt, style, imageBytes);
                        designRepository.save(design);
                    } catch (IllegalArgumentException e) {
                        log.error("Invalid Base64 data: {}", line);
                        throw new IOException("Malformed image data from Python script");
                    }
                }
            }
        }

        try {

            process = pb.start();
            String imagePath = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            ).readLine();

            if (process.waitFor() != 0) {
                String error = new BufferedReader(
                        new InputStreamReader(process.getErrorStream())
                ).lines().collect(Collectors.joining("\n"));
                throw new RuntimeException("AI generation failed:\n" + error);
            }

            return Files.readAllBytes(Paths.get(imagePath));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Generation interrupted", e);
        }
    }

    private Design createDesignEntity(String prompt, String style, byte[] imageData) {
        Design design = new Design();
        design.setId(UUID.randomUUID());
        design.setPrompt(prompt);
        design.setStyle(style);
        design.setImageData(imageData);
        //design.setThumbnail(generateThumbnail(imageData));
        design.setPublic(true); // Default to public for marketplace
        return design;
    }

    private byte[] generateThumbnail(byte[] imageData) {
        // Implement proper thumbnail generation logic here
        // For now, return the original image (simplified)
        return imageData;

        /*
        // Example using ImageIO (would need additional dependencies)
        try {
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
            BufferedImage thumbnail = new BufferedImage(100, 100, originalImage.getType());
            Graphics2D g = thumbnail.createGraphics();
            g.drawImage(originalImage, 0, 0, 100, 100, null);
            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            log.warn("Failed to generate thumbnail, using original image", e);
            return imageData;
        }
        */
    }
}