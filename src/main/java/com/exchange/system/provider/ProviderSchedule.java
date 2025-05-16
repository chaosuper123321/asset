package com.exchange.system.provider;


import com.exchange.system.constants.Constants;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProviderSchedule {
    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public static void start(BlockingDeque<byte[]> blockingDeque) {
        Runnable task = new ProviderThread(blockingDeque);
        scheduler.scheduleAtFixedRate(task,
                Constants.PROVIDER_SCHEDULE_DELAY_SECONDS,
                Constants.PROVIDER_SCHEDULE_PERIOD_SECONDS,
                TimeUnit.SECONDS);
    }
    public static void shutdown() {
        scheduler.shutdown();
    }
}
