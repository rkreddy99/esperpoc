package com.example.esperpoc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StrategyReadyEvent extends BaseEvent {
    private String id;
    private String strategy;
    private String date;
    private String exchange;
    private String hash;
}