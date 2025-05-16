package com.exchange.system.assets;

import com.exchange.system.data.db.sqllite.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class DBTest {

    @Test
    public void testUpdate() {
        DB.createDatabaseAndInitData();
        String symbol = "AAPL";
        double price = 120.0;
        DB.updateStockPrice(symbol, price);
        Assertions.assertEquals(price, DB.getDataBySymbol(symbol).getCurPrice());
    }

    
}
