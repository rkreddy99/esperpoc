package com.example.esperpoc.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public abstract class BaseEvent {
    @NotNull(message = "Event type is required")
    private String eventType;
    @NotNull(message = "Event ID is required")
    private String id;
    private LocalDateTime timestamp;
}
