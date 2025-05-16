package com.exchange.system.application;

import com.exchange.system.data.PositionData;
import com.exchange.system.data.sqlite.DatabaseManager;
import com.exchange.system.provider.MarketDataProvider;
import com.exchange.system.subscriber.MarketDataSubscriberThread;
import com.exchange.system.data.csv.PositionCsvParser;
import java.util.TreeMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.exchange.system"})
public class ExchangeAssetsApplication {

    @Autowired
    MarketDataProvider marketDataProvider;
    private static final int PRICE_MESSAGES_QUEUE_CAPACITY = 1024;
    private final BlockingDeque<byte[]> priceMessages
            = new LinkedBlockingDeque<>(PRICE_MESSAGES_QUEUE_CAPACITY);

    public static void main(String[] args) {
        SpringApplication.run(ExchangeAssetsApplication.class, args);
    }

    @PostConstruct
    public void initializeDataAndStartProvider() {
        TreeMap<String, Double> positionData = PositionCsvParser.loadPositions();
        if (positionData != null) {
            PositionData.getInstance().updatePositions(positionData);
        } else {
            throw new IllegalStateException("Position data CSV file not found at "
                    + "asset/src/main/resources/position_file.csv");
        }

        boolean isInitialized = DatabaseManager.createDatabaseAndInitData();
        if (!isInitialized) {
            throw new IllegalStateException("Failed to initialize securities table"
                    + " in SQLite database");
        }

        marketDataProvider.start(priceMessages);
    }

    @Bean
    public CommandLineRunner startSubscriptionThread(
            Executor taskExecutor) {
        return args -> {
            taskExecutor.execute(new MarketDataSubscriberThread(priceMessages));
        };
    }

    @PreDestroy
    public void shutdown() {
        priceMessages.clear();
    }
}
