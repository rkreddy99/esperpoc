package com.example.esperpoc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class EventA extends BaseEvent {
    @NotNull(message = "Strategy is required")
    private String strategy;
    @NotNull(message = "Date is required")
    private String date;
}
