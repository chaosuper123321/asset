package com.exchange.system.application;


import com.exchange.system.utils.NumberUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberUtilsTest {

    @Test
    public void testRoundWithMath() {
        double value = 123.45678;
        int places = 2;
        Assertions.assertTrue(NumberUtils.roundToDecimalPlaces(value, places) == 123.46);
    }
}
