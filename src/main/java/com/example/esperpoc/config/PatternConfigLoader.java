package com.example.esperpoc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Paths;

@Component
@Slf4j
public class PatternConfigLoader {
    // private final ObjectMapper objectMapper;
    // private final String configPath = "config/patterns.json";

    // public PatternConfigLoader(ObjectMapper objectMapper) {
    //     this.objectMapper = objectMapper;
    // }

    // public List<PatternConfig> loadPatterns() {
    //     try {
    //         String content = Files.readString(Paths.get(configPath));
    //         PatternConfigWrapper wrapper = objectMapper.readValue(content, PatternConfigWrapper.class);
    //         return wrapper.getPatterns();
    //     } catch (Exception e) {
    //         log.error("Failed to load pattern configurations", e);
    //         return new ArrayList<>();
    //     }
    // }

    // @Data
    // private static class PatternConfigWrapper {
    //     private List<PatternConfig> patterns;
    // }
}
