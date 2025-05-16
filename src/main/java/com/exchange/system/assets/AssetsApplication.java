package com.exchange.system.assets;

import com.exchange.system.data.csv.PositionData;
import com.exchange.system.data.db.sqllite.DB;
import com.exchange.system.provider.ProviderSchedule;
import com.exchange.system.subscriber.SubscribeThread;
import com.exchange.system.utils.CsvUtil;
import java.util.TreeMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.exchange.system"})
public class AssetsApplication {


    private final BlockingDeque<byte[]> priceMessages = new LinkedBlockingDeque<>(1024);
    public static void main(String[] args) {
        SpringApplication.run(AssetsApplication.class, args);
    }

    @PostConstruct
    public void initDataAndStartProvide() {
        TreeMap<String, Double> data = CsvUtil.getPositionDataFromCSV();
        if (data != null) {
            PositionData.getInstance().setData(data);
        } else {
            throw new IllegalStateException("CSV file not found");
        }

        boolean ret = DB.createDatabaseAndInitData();
        if (!ret) {
            throw new IllegalStateException("Failed to read stock data from SQLite");
        }

        ProviderSchedule.start(priceMessages);
    }

    @Bean
    public CommandLineRunner startSubscriber(
            Executor taskExecutor) {
        return args -> {
            taskExecutor.execute(new SubscribeThread(priceMessages));
        };
    }

    @PreDestroy
    public void shutdown() {
        ProviderSchedule.shutdown();
        priceMessages.clear();
    }
}
