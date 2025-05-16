package com.exchange.system.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class SubscriberTaskExecutorConfig {

    @Bean("taskExecutor")
    public Executor configureTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(1024);
        executor.setThreadNamePrefix("SubscriberThread-");
        executor.initialize();
        return executor;
    }
}
