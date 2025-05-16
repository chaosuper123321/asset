package com.exchange.system.subscriber;

import com.exchange.system.protocol.Message.ProviderMessage;
import com.exchange.system.protocol.Message.ProviderMessage.MessageItem;
import com.exchange.system.display.MarketDataDisplay;
import com.google.protobuf.InvalidProtocolBufferException;
import java.util.List;
import java.util.concurrent.BlockingDeque;

public class MarketDataSubscriberThread implements Runnable {

    private volatile boolean running = true;
    private final BlockingDeque<byte[]> priceMessages;

    public MarketDataSubscriberThread(BlockingDeque<byte[]> priceMessages) {
        if (priceMessages == null) {
            throw new IllegalArgumentException("Price messages queue cannot be null");
        }
        this.priceMessages = priceMessages;
    }

    @Override
    public void run() {
        int displayIndex = 1;
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                byte[] message = priceMessages.take();
                ProviderMessage providerMessage = ProviderMessage.parseFrom(message);
                List<MessageItem> messageItems = providerMessage.getItemsList();
                MarketDataDisplay.displayMarketUpdate(displayIndex, messageItems);
                displayIndex++;

            } catch (InterruptedException | InvalidProtocolBufferException e) {
                break;
            }
        }
    }

    public void stop() {
        running = false;
        Thread.currentThread().interrupt();
    }
}
