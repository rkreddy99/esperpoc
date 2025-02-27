package com.example.esperpoc.service;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompiler;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.espertech.esper.runtime.client.*;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.PostConstruct;

import java.util.Map;
import com.espertech.esper.common.client.util.NameAccessModifier;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class PatternManager {
    private final EPRuntime runtime;
    private final Map<String, EPDeployment> deployments = new ConcurrentHashMap<>();

    public PatternManager(EPRuntime runtime) {
        this.runtime = runtime;
    }

    @PostConstruct
    public void init() {
        // First deploy all event windows
        deployEventAWindow();
        deployEventBWindow();
        deployEventCWindow();
        
        // Then deploy patterns that depend on these windows
        deployStrategyPattern();
        deployMarketDataPattern();
        // deployTradingPattern();
        String[] deployedPatterns = runtime.getDeploymentService().getDeployments();
        for (String pattern : deployedPatterns) {
            // log pattern details
            EPDeployment deployment = runtime.getDeploymentService().getDeployment(pattern);
            log.info("Deployment name: {}", deployment.getModuleName());
            log.info("Deployment properties: {}", deployment.getModuleProperties().toString());
        }
    }

    private void deployEventAWindow() {
        // Check if already deployed to avoid duplicates
        if (deployments.containsKey("EVENT_A_WINDOW")) {
            return;
        }
        
        String epl = """
        @public
        create window EventAWindow.win:time(48 hours) as EventA;
        insert into EventAWindow select * from EventA;
        """;
        
        deployPatternWithRuntimePath("EVENT_A_WINDOW", epl);
    }

    private void deployEventBWindow() {
        // Check if already deployed to avoid duplicates
        if (deployments.containsKey("EVENT_B_WINDOW")) {
            return;
        }
        
        String epl = """
        @public
        create window EventBWindow.win:time(48 hours) as EventB;
        insert into EventBWindow select * from EventB;
        """;
        
        deployPatternWithRuntimePath("EVENT_B_WINDOW", epl);
    }

    private void deployEventCWindow() {
        // Check if already deployed to avoid duplicates
        if (deployments.containsKey("EVENT_C_WINDOW")) {
            return;
        }
        
        String epl = """
        @public
        create window EventCWindow.win:time(48 hours) as EventC;
        insert into EventCWindow select * from EventC;
        """;
        
        deployPatternWithRuntimePath("EVENT_C_WINDOW", epl);
    }

    private void deployStrategyPattern() {
        String epl = """
        @name('StrategyReadyPattern')
        INSERT INTO StrategyReadyEvent
        SELECT 
            a.id || '_' || b.id || '_' || c.id as id,
            a.date as date,
            a.strategy as strategy,
            b.exchange as exchange,
            c.hash as hash
        FROM 
            EventAWindow a
            join EventBWindow b on a.strategy = b.strategy and a.date = b.date
            join EventCWindow c on a.strategy = c.strategy
        """;
        
        deployPatternWithRuntimePath("STRATEGY_READY", epl);
    }

    private void deployMarketDataPattern() {
        String epl = """
            @name('MarketDataReadyPattern')
            INSERT INTO MarketDataReadyEvent
            SELECT 
                p.symbol as symbol,
                p.exchange as exchange,
                'ready' as status,
                p.timestamp as timestamp
            FROM pattern [
                every (
                    p=PriceSyncEvent(status='completed') -> 
                    v=VolumeSyncEvent(
                        symbol=p.symbol and 
                        exchange=p.exchange and 
                        status='completed'
                    ) ->
                    val=ValidationEvent(
                        symbol=p.symbol and 
                        exchange=p.exchange and 
                        status='success'
                    )
                ) where timer:within(5 minutes)
            ]
            """;
        deployPatternWithRuntimePath("MARKET_DATA_READY", epl);
    }

    // private void deployTradingPattern() {
    //     String epl = """
    //     @name('TradingAlertPattern')
    //     INSERT INTO TradingAlertEvent
    //     SELECT 
    //         o.symbol as symbol,
    //         o.orderId as orderId,
    //         p.alertType as alertType,
    //         o.timestamp as timestamp
    //     FROM pattern [
    //         every (
    //             o=OrderEvent -> 
    //             t=TradeEvent(
    //                 orderId=o.orderId
    //             ) ->
    //             p=PriceAlertEvent(
    //                 symbol=o.symbol
    //             )
    //         ) where timer:within(1 minute)
    //     ]
    //     """;
    //     deployPattern("TRADING_ALERT", epl);
    // }

    @SuppressWarnings("unused")
    private void deployPattern(String patternId, String epl) {
        try {
            EPCompiler compiler = EPCompilerProvider.getCompiler();
            CompilerArguments args = new CompilerArguments(runtime.getConfigurationDeepCopy());
            EPCompiled compiled = compiler.compile(epl, args);
            
            EPDeployment deployment = runtime.getDeploymentService().deploy(compiled);
            
            deployments.put(patternId, deployment);
            
            // Add listeners for the pattern
            for (EPStatement stmt : deployment.getStatements()) {
                stmt.addListener((newEvents, oldEvents, statement, runtime) -> {
                    if (newEvents != null) {
                        for (EventBean event : newEvents) {
                            log.info("Pattern {} matched: {}", patternId, event.getUnderlying());
                        }
                    }
                });
            }
            
            log.info("Successfully deployed pattern: {}", patternId);
        } catch (Exception e) {
            log.error("Failed to deploy pattern: " + patternId, e);
        }
    }

    private void deployPatternWithRuntimePath(String patternId, String epl) {
        try {
            EPCompiler compiler = EPCompilerProvider.getCompiler();
            CompilerArguments args = new CompilerArguments(runtime.getConfigurationDeepCopy());
            
            // Add the entire runtime path instead of individual dependencies
            args.getPath().add(runtime.getRuntimePath());
            
            // Make sure named windows are public
            args.getOptions().setAccessModifierNamedWindow(env -> NameAccessModifier.PUBLIC);
            
            EPCompiled compiled = compiler.compile(epl, args);
            
            EPDeployment deployment = runtime.getDeploymentService().deploy(compiled);
            
            deployments.put(patternId, deployment);
            
            // Add listeners for the pattern
            for (EPStatement stmt : deployment.getStatements()) {
                stmt.addListener((newEvents, oldEvents, statement, runtime) -> {
                    if (newEvents != null) {
                        for (EventBean event : newEvents) {
                            log.info("Pattern {} matched: {}", patternId, event.getUnderlying());
                        }
                    }
                });
            }
            
            log.info("Successfully deployed pattern: {}", patternId);
        } catch (Exception e) {
            log.error("Failed to deploy pattern: " + patternId, e);
        }
    }
}