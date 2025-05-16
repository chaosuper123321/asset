package com.exchange.system.provider;

import com.exchange.system.data.sqlite.DatabaseManager;
import com.exchange.system.model.ProductType;
import com.exchange.system.model.SecurityData;
import com.exchange.system.pricing.EuropeanOptionPriceCalculator;
import com.exchange.system.pricing.SecurityPriceCalculator;
import com.exchange.system.protocol.Message.ProviderMessage;
import com.exchange.system.protocol.Message.ProviderMessage.MessageItem;
import com.exchange.system.pricing.FinancialUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingDeque;

public class MarketDataUpdateThread implements Runnable {

    private BlockingDeque<byte[]> priceMessages;

    public MarketDataUpdateThread(BlockingDeque<byte[]> priceMessages) {
        this.priceMessages = priceMessages;
    }

    @Override
    public void run() {
        try {
            //0. load data from sqlite db
            List<SecurityData> securities = DatabaseManager.getAllSecurities();
            HashMap<String, Boolean> priceChangeMap = new HashMap<>();
            //1. calc new price
            HashMap<String, Double> securityPriceMap
                    = calculateNewPrices(securities, priceChangeMap);
            //2. create message item list
            List<MessageItem> messageItems = buildMessageItems(
                    securities, securityPriceMap, priceChangeMap);
            //3. create provider message
            ProviderMessage providerMessage = ProviderMessage.newBuilder()
                    .addAllItems(messageItems)
                    .build();
            //4. put message to block queue
            try {
                priceMessages.put(providerMessage.toByteArray());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, Double> calculateNewPrices(List<SecurityData> securities,
            HashMap<String, Boolean> priceChangeMap) {
        HashMap<String, Double> securityPriceMap = new HashMap<>();
        for (SecurityData securityData : securities) {
            if (securityData.getProductType().equals(ProductType.COMMON_STOCK)) {
                //calc common stock
                double newPrice = SecurityPriceCalculator.calculateUpdatedPrice(
                        securityData.getCurrentPrice(),
                        securityData.getExpectedReturn(),
                        securityData.getAnnualizedStandardDeviation()
                );
                if (!FinancialUtil.areEqual(newPrice, securityData.getCurrentPrice(), FinancialUtil.PRICE_COMPARISON_TOLERANCE)) {
                    priceChangeMap.put(securityData.getSymbol(), true);
                    DatabaseManager.updateSecurityPrice(securityData.getSymbol(), newPrice);
                }

                securityData.setCurrentPrice(newPrice);
                securityPriceMap.put(securityData.getSymbol(), newPrice);
            }
        }
        return securityPriceMap;
    }

    private List<MessageItem> buildMessageItems(List<SecurityData> securities
            , HashMap<String, Double> securityPriceMap
            , HashMap<String, Boolean> priceChangeMap) {
        List<MessageItem> messageItems = new ArrayList<>();

        for (SecurityData securityData : securities) {
            String[] symbolParts = securityData.getSymbol().split("-");
            double basePrice = securityPriceMap.getOrDefault(symbolParts[0], 0.0);
            if (securityData.getProductType().equals(ProductType.EUROPEAN_PUT_OPTION)) {
                //calc put option
                double newPutPrice = EuropeanOptionPriceCalculator.calculatePutPrice(
                        basePrice,
                        securityData.getStrikePrice(),
                        securityData.getAnnualizedStandardDeviation(),
                        securityData.getMaturity()
                );
                MessageItem messageItem = MessageItem.newBuilder()
                        .setSymbol(securityData.getSymbol())
                        .setPrice(newPutPrice)
                        .build();
                messageItems.add(messageItem);
            } else if (securityData.getProductType().equals(ProductType.EUROPEAN_CALL_OPTION)) {
                //calc call option
                double newCallPrice = EuropeanOptionPriceCalculator.calculateCallPrice(
                        basePrice,
                        securityData.getStrikePrice(),
                        securityData.getAnnualizedStandardDeviation(),
                        securityData.getMaturity()
                );
                MessageItem messageItem = MessageItem.newBuilder()
                        .setSymbol(securityData.getSymbol())
                        .setPrice(newCallPrice)
                        .build();
                messageItems.add(messageItem);
            } else if (securityData.getProductType().equals(ProductType.COMMON_STOCK)) {
                boolean priceChange =
                        priceChangeMap.containsKey(securityData.getSymbol()) ? true : false;
                MessageItem messageItem = MessageItem.newBuilder()
                        .setSymbol(securityData.getSymbol())
                        .setPrice(securityData.getCurrentPrice())
                        .setPriceChange(priceChange)
                        .build();
                messageItems.add(messageItem);
            }
        }

        return messageItems;
    }

}
