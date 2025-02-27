package com.example.esperpoc.util;

import com.example.esperpoc.model.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestEventGenerator {
    public static EventA createEventA(String strategy, String date) {
        EventA event = new EventA();
        event.setEventType("A");
        event.setStrategy(strategy);
        event.setDate(date);
        event.setTimestamp(LocalDateTime.now());
        return event;
    }

    public static EventB createEventB(String strategy, String date, String exchange) {
        EventB event = new EventB();
        event.setEventType("B");
        event.setStrategy(strategy);
        event.setDate(date);
        event.setExchange(exchange);
        event.setTimestamp(LocalDateTime.now());
        return event;
    }

    public static EventC createEventC(String strategy, String hash) {
        EventC event = new EventC();
        event.setEventType("C");
        event.setStrategy(strategy);
        event.setHash(hash);
        event.setTimestamp(LocalDateTime.now());
        return event;
    }

    public static PriceSyncEvent createPriceSyncEvent(String symbol, String exchange) {
        PriceSyncEvent event = new PriceSyncEvent();
        event.setEventType("PriceSync");
        event.setSymbol(symbol);
        event.setExchange(exchange);
        event.setStatus("completed");
        event.setRecordCount(1000);
        event.setTimestamp(LocalDateTime.now());
        return event;
    }

    public static VolumeSyncEvent createVolumeSyncEvent(String symbol, String exchange) {
        VolumeSyncEvent event = new VolumeSyncEvent();
        event.setEventType("VolumeSync");
        event.setSymbol(symbol);
        event.setExchange(exchange);
        event.setStatus("completed");
        event.setRecordCount(1000);
        event.setTimestamp(LocalDateTime.now());
        return event;
    }

    public static ValidationEvent createValidationEvent(String symbol, String exchange) {
        ValidationEvent event = new ValidationEvent();
        event.setEventType("Validation");
        event.setSymbol(symbol);
        event.setExchange(exchange);
        event.setStatus("success");
        event.setTimestamp(LocalDateTime.now());
        return event;
    }

    public static OrderEvent createOrderEvent(String symbol, Double price, Integer quantity) {
        OrderEvent event = new OrderEvent();
        event.setEventType("Order");
        event.setOrderId(UUID.randomUUID().toString());
        event.setSymbol(symbol);
        event.setSide("BUY");
        event.setQuantity(quantity);
        event.setPrice(price);
        event.setTimestamp(LocalDateTime.now());
        return event;
    }

    public static TradeEvent createTradeEvent(String orderId, String symbol, Integer executedQty) {
        TradeEvent event = new TradeEvent();
        event.setEventType("Trade");
        event.setTradeId(UUID.randomUUID().toString());
        event.setOrderId(orderId);
        event.setSymbol(symbol);
        event.setExecutedQty(executedQty);
        event.setTimestamp(LocalDateTime.now());
        return event;
    }

    public static PriceAlertEvent createPriceAlertEvent(String symbol, Double price) {
        PriceAlertEvent event = new PriceAlertEvent();
        event.setEventType("PriceAlert");
        event.setSymbol(symbol);
        event.setAlertType("threshold_breach");
        event.setPrice(price);
        event.setTimestamp(LocalDateTime.now());
        return event;
    }
}