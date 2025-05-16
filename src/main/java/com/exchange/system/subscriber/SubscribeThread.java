package com.exchange.system.subscriber;

import com.exchange.system.protocol.Message.ProviderMessage;
import com.exchange.system.viewer.OutputAssets;
import com.google.protobuf.InvalidProtocolBufferException;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import com.exchange.system.protocol.Message.ProviderMessage.*;

public class SubscribeThread implements Runnable {
    private BlockingDeque<byte[]> blockingDeque;
    public SubscribeThread(BlockingDeque<byte[]> blockingDeque) {
        this.blockingDeque = blockingDeque;
    }
    @Override
    public void run() {
        int index = 1;
        while(true) {
            try {
                byte[] message = blockingDeque.take();
                if (message == null) {
                    Thread.sleep(100);
                    break;
                } else {
                    ProviderMessage providerMessage = ProviderMessage.parseFrom(message);
                    List<MessageItem> messageItemList =  providerMessage.getItemsList();
                    OutputAssets.display(index, messageItemList);
                    index ++;
                }
            } catch (InterruptedException | InvalidProtocolBufferException e) {
                break;
            }
        }
    }
}
