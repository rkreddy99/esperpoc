package com.example.esperpoc.service;

import com.espertech.esper.runtime.client.EPRuntime;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.example.esperpoc.model.*;

@Service
@Slf4j
public class EventService {
    private final EPRuntime runtime;

    public EventService(EPRuntime runtime) {
        this.runtime = runtime;
    }

    public void processEvent(Object event) {
        try {
            if (event instanceof BaseEvent) {
                // Get the actual class name instead of eventType
                String className = event.getClass().getSimpleName();
                
                runtime.getEventService().sendEventBean(event, className);
                log.debug("Processed event: {}", event);
            } else {
                log.error("Invalid event type: {}", event.getClass());
                throw new IllegalArgumentException("Event must be a BaseEvent type");
            }
        } catch (Exception e) {
            log.error("Error processing event: {}", event, e);
            throw new RuntimeException("Failed to process event", e);
        }
    }

    public void processStrategyEvent(StrategyReadyEvent event) {
        log.info("Processing Strategy Event: {}", event);
        processEvent(event);
    }

    public void processMarketDataEvent(MarketDataReadyEvent event) {
        log.info("Processing Market Data Event: {}", event);
        processEvent(event);
    }

    public void processTradingEvent(TradingAlertEvent event) {
        log.info("Processing Trading Event: {}", event);
        processEvent(event);
    }
}
