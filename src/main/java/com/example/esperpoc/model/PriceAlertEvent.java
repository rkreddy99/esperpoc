package com.example.esperpoc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@EqualsAndHashCode(callSuper = true)
public class PriceAlertEvent extends BaseEvent {
    @NotNull(message = "Symbol is required")
    private String symbol;
    @NotNull(message = "AlertType is required")
    private String alertType;
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;
}
