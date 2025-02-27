package com.example.esperpoc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@EqualsAndHashCode(callSuper = true)
public class TradeEvent extends BaseEvent {
    @NotNull(message = "TradeId is required")
    private String tradeId;
    @NotNull(message = "OrderId is required")
    private String orderId;
    @NotNull(message = "Symbol is required")
    private String symbol;
    @NotNull(message = "ExecutedQty is required")
    @Positive(message = "ExecutedQty must be positive")
    private Integer executedQty;
}
