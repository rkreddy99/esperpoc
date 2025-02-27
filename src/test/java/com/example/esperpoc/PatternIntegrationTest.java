package com.example.esperpoc;

import com.example.esperpoc.model.*;
import com.example.esperpoc.service.EventService;
import com.example.esperpoc.util.TestEventGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PatternIntegrationTest {

    @Autowired
    private EventService eventService;

    @Test
    public void testStrategyPattern() throws InterruptedException {
        // Create test events
        EventA eventA = TestEventGenerator.createEventA("momentum_v2", "2025-02-24");
        EventB eventB = TestEventGenerator.createEventB("momentum_v2", "2025-02-24", "NYSE");
        EventC eventC = TestEventGenerator.createEventC("momentum_v2", "abc123");

        // Send events
        eventService.processEvent(eventA);
        TimeUnit.SECONDS.sleep(1);
        eventService.processEvent(eventB);
        TimeUnit.SECONDS.sleep(1);
        eventService.processEvent(eventC);

        // Wait for pattern matching
        TimeUnit.SECONDS.sleep(20);
        // Add assertions based on your pattern output listener
    }

    @Test
    public void testMarketDataPattern() throws InterruptedException {
        String symbol = "AAPL";
        String exchange = "NYSE";

        // Create test events
        PriceSyncEvent priceSync = TestEventGenerator.createPriceSyncEvent(symbol, exchange);
        VolumeSyncEvent volumeSync = TestEventGenerator.createVolumeSyncEvent(symbol, exchange);
        ValidationEvent validation = TestEventGenerator.createValidationEvent(symbol, exchange);

        // Send events
        eventService.processEvent(priceSync);
        TimeUnit.SECONDS.sleep(1);
        eventService.processEvent(volumeSync);
        TimeUnit.SECONDS.sleep(1);
        eventService.processEvent(validation);

        // Wait for pattern matching
        TimeUnit.SECONDS.sleep(2);
        // Add assertions based on your pattern output listener
    }

    @Test
    public void testTradingPattern() throws InterruptedException {
        String symbol = "AAPL";
        Double price = 150.00;
        
        // Create test events
        OrderEvent order = TestEventGenerator.createOrderEvent(symbol, price, 100);
        TradeEvent trade = TestEventGenerator.createTradeEvent(order.getOrderId(), symbol, 50);
        PriceAlertEvent alert = TestEventGenerator.createPriceAlertEvent(symbol, price);

        // Send events
        eventService.processEvent(order);
        TimeUnit.SECONDS.sleep(1);
        eventService.processEvent(trade);
        TimeUnit.SECONDS.sleep(1);
        eventService.processEvent(alert);

        // Wait for pattern matching
        TimeUnit.SECONDS.sleep(2);
        // Add assertions based on your pattern output listener
    }
}