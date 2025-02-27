package com.example.esperpoc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderEvent extends BaseEvent {
    @NotNull(message = "OrderId is required")
    private String orderId;
    @NotNull(message = "Symbol is required")
    private String symbol;
    @NotNull(message = "Side is required")
    private String side;
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;
}
