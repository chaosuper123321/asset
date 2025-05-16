package com.exchange.system.display;

import com.exchange.system.data.PositionData;
import com.exchange.system.protocol.Message.ProviderMessage.MessageItem;
import com.exchange.system.utils.NumberUtils;
import java.util.HashMap;
import java.util.List;

public class MarketDataDisplay {

    public static void displayMarketUpdate(int index, List<MessageItem> messageItems) {
        if (messageItems == null || messageItems.isEmpty()) {
            System.err.println("No market data to display for index " + index);
            return;
        }

        HashMap<String, Double> positions = PositionData.getInstance().getPositions();

        System.out.println(
                AnsiColor.RED.format("## " + index + " Market Data Update")
        );
        displayPriceChanges(messageItems);

        System.out.printf("%-20s%15s%10s%15s%n", "symbol", "price", "qty", "value");
        System.out.println("------------------------------------------------------------");

        double total = 0.0;
        for (MessageItem messageItem : messageItems) {
            double price = messageItem.getPrice();
            Double quantity = positions.get(messageItem.getSymbol());
            if(quantity == null) {
                continue;
            }
            double positionValue = price * quantity;
            total = total + positionValue;

            String outputLine = String.format("%-20s%15.2f%10.2f%15.2f%n"
                    , messageItem.getSymbol()
                    , NumberUtils.roundToDecimalPlaces(price, 2)
                    , NumberUtils.roundToDecimalPlaces(quantity, 2)
                    , NumberUtils.roundToDecimalPlaces(positionValue,2));
            System.out.printf(AnsiColor.MAGENTA.format(outputLine));
        }

        System.out.println("------------------------------------------------------------");
        displayTotal(total);

    }

    public static void displayPriceChanges(List<MessageItem> messageItems) {
        for (MessageItem messageItem : messageItems) {
            if (messageItem.getPriceChange()) {
                String output = String.format("%s change to %.2f\n"
                        , messageItem.getSymbol()
                        , NumberUtils.roundToDecimalPlaces(messageItem.getPrice(),2));
                System.out.printf(AnsiColor.YELLOW.format(output));
            }
        }
        System.out.println("");
    }

    public static void displayTotal(double total ) {
        String output = String.format("%-20s%15s%10s%15.2f%n\n\n"
                , "Total portfolio"
                , "", ""
                , NumberUtils.roundToDecimalPlaces(total,2));
        System.out.println(AnsiColor.GREEN.format(output));
    }
}
