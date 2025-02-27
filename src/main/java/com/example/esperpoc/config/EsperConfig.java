package com.example.esperpoc.config;

import com.example.esperpoc.model.*;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@org.springframework.context.annotation.Configuration
@ComponentScan("com.example.esperpoc")
public class EsperConfig {

    @Bean
    public EPRuntime esperRuntime() {
        Configuration configuration = new Configuration();
        
        // Register Strategy Events
        configuration.getCommon().addEventType(EventA.class);
        configuration.getCommon().addEventType(EventB.class);
        configuration.getCommon().addEventType(EventC.class);
        configuration.getCommon().addEventType(StrategyReadyEvent.class);
        
        // Register Market Data Events
        configuration.getCommon().addEventType(PriceSyncEvent.class);
        configuration.getCommon().addEventType(VolumeSyncEvent.class);
        configuration.getCommon().addEventType(ValidationEvent.class);
        configuration.getCommon().addEventType(MarketDataReadyEvent.class);
        
        // Register Trading Events
        configuration.getCommon().addEventType(OrderEvent.class);
        configuration.getCommon().addEventType(TradeEvent.class);
        configuration.getCommon().addEventType(PriceAlertEvent.class);
        configuration.getCommon().addEventType(TradingAlertEvent.class);
        
        return EPRuntimeProvider.getDefaultRuntime(configuration);
    }
}