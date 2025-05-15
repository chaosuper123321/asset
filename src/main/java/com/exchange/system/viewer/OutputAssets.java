package com.exchange.system.viewer;


import com.exchange.system.data.csv.PositionData;
import com.exchange.system.protocol.Message.ProviderMessage.*;
import java.util.List;
import java.util.TreeMap;

public class OutputAssets {

    //TODO, add 四舍五入
    public static void display(int index, List<MessageItem> messageItemList ) {
        TreeMap<String, Float> csvData =  PositionData.getInstance().getData();

        System.out.println("## " +index+ " Market Data Update");
        displayStockChange(messageItemList);

        System.out.printf("%-20s%15s%10s%15s%n", "symbol", "price", "qty", "value");
        System.out.println("------------------------------------------------------------");

        double total = 0.0;
        for (MessageItem messageItem : messageItemList) {
            double price = messageItem.getPrice();
            double qty = csvData.get(messageItem.getSymbol());
            double oneProto = price * qty;
            total = total + oneProto;

            System.out.printf("%-20s%15.2f%10.2f%15.2f%n",
                    messageItem.getSymbol(), price, qty, oneProto);
        }

        System.out.println("------------------------------------------------------------");

        System.out.printf("%-20s%15s%10s%15.2f%n\n\n", "Total portfolio", "", "", total);
    }

    public static void displayStockChange(List<MessageItem> messageItemList) {
        for (MessageItem messageItem : messageItemList) {
            if (messageItem.getPriceChange()) {
                System.out.printf("%s change to %.2f\n"
                        , messageItem.getSymbol()
                        , messageItem.getPrice());
            }
        }
        System.out.println("");
    }

}
