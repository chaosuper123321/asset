package com.exchange.system.assets;

import com.exchange.system.data.csv.PositionData;
import com.exchange.system.data.db.sqllite.DB;
import com.exchange.system.provider.ProviderSchedule;
import com.exchange.system.subscriber.SubscribeThread;
import com.exchange.system.utils.CsvUtil;
import java.util.TreeMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class AssetsApplication {

    public static void main(String[] args) {
        // 1.read positionfile.csv file at path : assets/src/main/resources/
        TreeMap<String, Float> data = CsvUtil.getPositionDataFromCSV();
        if (data != null) {
            PositionData.getInstance().setData(data);
        } else {
            System.err.println("csv file not found");
            return;
        }

        // 2.read stock data from SQLite
        boolean ret = DB.createDatabaseAndInitData();
        if (!ret) {
            System.err.println("read stock data from sqllite failed");
            return;
        }

        // 3. provider calc price and publish price info
        BlockingDeque<byte[]> priceMessages = new LinkedBlockingDeque<>();
        ProviderSchedule.start(priceMessages);

        // 4. subscribe result
        Thread subscribeThread = new Thread(new SubscribeThread(priceMessages));
        subscribeThread.start();

        try {
            subscribeThread.join();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            Thread.currentThread().interrupt();
        }

        // 5. add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ProviderSchedule.shutdown();
            subscribeThread.interrupt();
            priceMessages.clear();
        }));
    }

}
