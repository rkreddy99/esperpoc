package com.example.esperpoc.service;

import com.example.esperpoc.config.PatternConfig;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PatternBuilderService {

    public String buildEPL(PatternConfig config) {
        StringBuilder epl = new StringBuilder();
        epl.append("@name('").append(config.getPatternId()).append("')\n");
        epl.append("INSERT INTO ").append(config.getOutputEvent().getType()).append("\n");
        epl.append("SELECT \n");

        // Add output mappings
        config.getOutputEvent().getMapping().forEach((target, source) -> {
            epl.append("    ").append(source).append(" as ").append(target).append(",\n");
        });
        // Remove last comma
        epl.setLength(epl.length() - 2);
        epl.append("\n");

        // Build pattern
        epl.append("FROM pattern [\n");
        epl.append("    every (\n");
        buildPatternConditions(epl, config);
        epl.append("    )\n");
        epl.append("]");

        return epl.toString();
    }

    private void buildPatternConditions(StringBuilder epl, PatternConfig config) {
        List<String> events = config.getInputEvents();
        for (int i = 0; i < events.size(); i++) {
            String event = events.get(i);
            epl.append("        ");
            if (i > 0) epl.append("-> ");
            
            epl.append(event.toLowerCase()).append("=").append(event);
            
            // Add conditions from correlation rules
            final int index = i;
            config.getCorrelationRules().getConditions().forEach(condition -> {
                if (condition.getMatchAcross().contains(event)) {
                    if (index > 0) {
                        String previousEvent = events.get(index-1).toLowerCase();
                        epl.append(" and ").append(condition.getField())
                           .append("=").append(previousEvent).append(".").append(condition.getField());
                    }
                }
            });
            
            epl.append("\n");
        }
    }
}
