package com.example.esperpoc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MarketDataReadyEvent extends BaseEvent {
    private String symbol;
    private String exchange;
    private String status;
    private Integer recordCount;
}