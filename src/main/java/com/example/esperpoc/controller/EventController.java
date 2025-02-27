package com.example.esperpoc.controller;

import com.example.esperpoc.model.*;
import com.example.esperpoc.service.EventService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@RestController
@RequestMapping("/api/events")
@Slf4j
public class EventController {
    private final EventService eventService;
    private final ObjectMapper objectMapper;

    public EventController(EventService eventService, ObjectMapper objectMapper) {
        this.eventService = eventService;
        this.objectMapper = objectMapper;
    }

    /**
     * Process a single event (keeping original endpoint for backward compatibility)
     */
    @PostMapping
    public ResponseEntity<String> processEvent(@RequestBody JsonNode eventJson) {
        try {
            if (!eventJson.has("eventType")) {
                return ResponseEntity.badRequest().body("Event type is required");
            }
            
            String eventType = eventJson.get("eventType").asText();
            BaseEvent event = convertToEvent(eventType, eventJson);
            
            if (event.getTimestamp() == null) {
                event.setTimestamp(LocalDateTime.now());
            }

            eventService.processEvent(event);
            return ResponseEntity.ok("Event processed successfully");
            
        } catch (Exception e) {
            log.error("Error processing event", e);
            return ResponseEntity.badRequest().body("Error processing event: " + e.getMessage());
        }
    }
    
    /**
     * Process a batch of events
     */
    @PostMapping("/batch")
    public ResponseEntity<?> processBatchEvents(@RequestBody JsonNode eventsJson) {
        try {
            if (!eventsJson.isArray()) {
                return ResponseEntity.badRequest().body("Request body must be an array of events");
            }
            
            ArrayNode eventsArray = (ArrayNode) eventsJson;
            List<BaseEvent> processedEvents = new ArrayList<>();
            Map<String, List<String>> errors = new HashMap<>();
            
            for (int i = 0; i < eventsArray.size(); i++) {
                JsonNode eventJson = eventsArray.get(i);
                try {
                    if (!eventJson.has("eventType")) {
                        addError(errors, i, "Event type is required");
                        continue;
                    }
                    
                    String eventType = eventJson.get("eventType").asText();
                    BaseEvent event = convertToEvent(eventType, eventJson);
                    
                    if (event.getTimestamp() == null) {
                        event.setTimestamp(LocalDateTime.now());
                    }
                    
                    processedEvents.add(event);
                } catch (Exception e) {
                    addError(errors, i, e.getMessage());
                }
            }
            
            // Process all valid events
            for (BaseEvent event : processedEvents) {
                try {
                    eventService.processEvent(event);
                } catch (Exception e) {
                    log.error("Error processing event: {}", event, e);
                    // We continue processing even if one event fails
                }
            }
            
            // Return appropriate response
            if (errors.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "message", "All events processed successfully",
                    "count", processedEvents.size()
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "message", "Processed events with some errors",
                    "successCount", processedEvents.size(),
                    "errors", errors
                ));
            }
            
        } catch (Exception e) {
            log.error("Error processing batch events", e);
            return ResponseEntity.badRequest().body("Error processing batch events: " + e.getMessage());
        }
    }

    private void addError(Map<String, List<String>> errors, int index, String message) {
        errors.computeIfAbsent(String.valueOf(index), k -> new ArrayList<>()).add(message);
    }

    private BaseEvent convertToEvent(String eventType, JsonNode eventJson) {
        return switch (eventType) {
            // Strategy Events
            case "A" -> objectMapper.convertValue(eventJson, EventA.class);
            case "B" -> objectMapper.convertValue(eventJson, EventB.class);
            case "C" -> objectMapper.convertValue(eventJson, EventC.class);
            
            // Market Data Events
            case "PriceSync" -> objectMapper.convertValue(eventJson, PriceSyncEvent.class);
            case "VolumeSync" -> objectMapper.convertValue(eventJson, VolumeSyncEvent.class);
            case "Validation" -> objectMapper.convertValue(eventJson, ValidationEvent.class);
            
            // Trading Events
            case "Order" -> objectMapper.convertValue(eventJson, OrderEvent.class);
            case "Trade" -> objectMapper.convertValue(eventJson, TradeEvent.class);
            case "PriceAlert" -> objectMapper.convertValue(eventJson, PriceAlertEvent.class);
            
            default -> throw new IllegalArgumentException("Unknown event type: " + eventType);
        };
    }
}