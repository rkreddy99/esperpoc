package com.example.esperpoc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationEvent extends BaseEvent {
    @NotNull(message = "Symbol is required")
    private String symbol;
    @NotNull(message = "Exchange is required")
    private String exchange;
    @NotNull(message = "Status is required")
    private String status;
}
