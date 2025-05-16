package com.exchange.system.provider;

import com.exchange.system.data.db.sqllite.DB;
import com.exchange.system.model.ProductType;
import com.exchange.system.model.StockData;
import com.exchange.system.protocol.Message.ProviderMessage;
import com.exchange.system.protocol.Message.ProviderMessage.MessageItem;
import com.exchange.system.utils.PriceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingDeque;

public class ProviderThread implements Runnable {
    private BlockingDeque<byte[]> blockingDeque;

    public ProviderThread(BlockingDeque<byte[]> blockingDeque) {
        this.blockingDeque = blockingDeque;
    }

    @Override
    public void run() {
        try {
            List<MessageItem> messageItemList = new ArrayList<>();
            HashMap<String, Boolean> priceChangeMap = new HashMap<>();

            List<StockData> data = DB.getData();
            HashMap<String, Double> stockPriceMap = new HashMap<>();
            for (StockData stockData : data) {
                if (stockData.getProductType().equals(ProductType.COMMON_STOCK)) {
                    //calc common stock
                    double newPrice = StockPriceCalc.calcNewPrice(
                            stockData.getCurPrice(),
                            stockData.getExpectedReturn(),
                            stockData.getAnnualizedStandardDeviation()
                    );
                    if (!PriceUtil.areEqual(newPrice, stockData.getCurPrice(), PriceUtil.EPSILON)) {
                        priceChangeMap.put(stockData.getSymbol(), true);
                        DB.updateStockPrice(stockData.getSymbol(), newPrice);
                    }

                    stockData.setCurPrice(newPrice);
                    stockPriceMap.put(stockData.getSymbol(), newPrice);
                }
            }

            for (StockData stockData : data) {
                String[] symbols = stockData.getSymbol().split("-");
                double stockPrice = stockPriceMap.get(symbols[0]);
                if (stockData.getProductType().equals(ProductType.EUROPEAN_PUT_OPTIONS)) {
                    //calc put option
                    double newPutPrice = EuropeanOptionPriceCalc.calcPutPrice(
                            stockPrice,
                            stockData.getStrikePrice(),
                            stockData.getAnnualizedStandardDeviation(),
                            stockData.getMaturity()
                    );
                    MessageItem messageItem = MessageItem.newBuilder()
                            .setSymbol(stockData.getSymbol())
                            .setPrice(newPutPrice)
                            .build();
                    messageItemList.add(messageItem);
                } else if (stockData.getProductType().equals(ProductType.EUROPEAN_CALL_OPTIONS)) {
                    //calc call option
                    double newCallPrice = EuropeanOptionPriceCalc.calcCallPrice(
                            stockPrice,
                            stockData.getStrikePrice(),
                            stockData.getAnnualizedStandardDeviation(),
                            stockData.getMaturity()
                    );
                    MessageItem messageItem = MessageItem.newBuilder()
                            .setSymbol(stockData.getSymbol())
                            .setPrice(newCallPrice)
                            .build();
                    messageItemList.add(messageItem);
                } else if (stockData.getProductType().equals(ProductType.COMMON_STOCK)) {
                    boolean priceChange = priceChangeMap.containsKey(stockData.getSymbol()) ? true : false;
                    MessageItem messageItem = MessageItem.newBuilder()
                            .setSymbol(stockData.getSymbol())
                            .setPrice(stockData.getCurPrice())
                            .setPriceChange(priceChange)
                            .build();
                    messageItemList.add(messageItem);
                }
            }
            ProviderMessage providerMessage = ProviderMessage.newBuilder()
                    .addAllItems(messageItemList)
                    .build();
            try {
                blockingDeque.put(providerMessage.toByteArray());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
