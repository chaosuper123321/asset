package com.exchange.system.application;

import com.exchange.system.data.sqlite.DatabaseManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DatabaseManagerTest {

    @Test
    public void testUpdate() {
        DatabaseManager.createDatabaseAndInitData();
        String symbol = "AAPL";
        double price = 120.0;
        DatabaseManager.updateSecurityPrice(symbol, price);
        Assertions.assertEquals(price, DatabaseManager.getSecurityBySymbol(symbol).getCurrentPrice());
    }
}
