package com.exchange.system.assets;

import com.exchange.system.data.db.sqllite.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DBTest {
    @Autowired
    private ApplicationContext context;

    @Test
    public void testUpdate() {
        DB db = context.getBean(DB.class);
        db.createDatabaseAndInitData();
        String symbol = "AAPL";
        double price = 120.0;
        db.updateStockPrice(symbol, price);
        Assertions.assertEquals(price, db.getDataBySymbol(symbol).getCurPrice());
    }
}
