package com.exchange.system.provider;

import com.exchange.system.constants.ExchangeSystemConstants;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.BlockingDeque;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class MarketDataProvider {

    @Autowired
    TaskScheduler marketDataScheduler;

    public void start(BlockingDeque<byte[]> priceMessages) {
        Runnable marketDataTask = new MarketDataUpdateThread(priceMessages);
        Instant startTime = Instant.now();
        Duration period = Duration.ofSeconds(ExchangeSystemConstants.PROVIDER_SCHEDULE_PERIOD_SECONDS);
        marketDataScheduler.scheduleAtFixedRate(marketDataTask, startTime, period);
    }
}
