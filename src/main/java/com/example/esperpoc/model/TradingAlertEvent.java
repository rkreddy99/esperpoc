package com.example.esperpoc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TradingAlertEvent extends BaseEvent {
    private String orderId;
    private String symbol;
    private String side;
    private Integer quantity;
    private Double price;
}