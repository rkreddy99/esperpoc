package com.example.esperpoc.config;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class PatternConfig {
    private String patternId;
    private String description;
    private String timeWindow;
    private List<String> inputEvents;
    private CorrelationRules correlationRules;
    private OutputEventMapping outputEvent;

    @Data
    public static class CorrelationRules {
        private List<MatchCondition> conditions;
    }

    @Data
    public static class MatchCondition {
        private String field;
        private List<String> matchAcross;
        private List<String> values;
    }

    @Data
    public static class OutputEventMapping {
        private String type;
        private Map<String, String> mapping;
    }
}
