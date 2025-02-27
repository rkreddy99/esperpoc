package com.example.esperpoc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Data
@EqualsAndHashCode(callSuper = true)
public class PriceSyncEvent extends BaseEvent {
    @NotNull(message = "Symbol is required")
    private String symbol;
    @NotNull(message = "Exchange is required")
    private String exchange;
    @NotNull(message = "Status is required")
    private String status;
    @NotNull(message = "Record count is required")
    @Min(value = 0, message = "Record count must be non-negative")
    private Integer recordCount;
}
