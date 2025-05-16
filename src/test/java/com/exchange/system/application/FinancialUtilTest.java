package com.exchange.system.application;

import com.exchange.system.pricing.FinancialUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class FinancialUtilTest {

    @Test
    public void testEqual() {
        Assertions.assertEquals(FinancialUtil.areEqual(0.1, 0.2, FinancialUtil.PRICE_COMPARISON_TOLERANCE), false);
        Assertions.assertEquals(FinancialUtil.areEqual(0.01, 0.02, FinancialUtil.PRICE_COMPARISON_TOLERANCE), false);
        Assertions.assertEquals(FinancialUtil.areEqual(0.001, 0.002, FinancialUtil.PRICE_COMPARISON_TOLERANCE), true);
        Assertions.assertEquals(FinancialUtil.areEqual(0.005, 0.006, FinancialUtil.PRICE_COMPARISON_TOLERANCE), true);
    }

    @Test
    public void testCalculateYearsToMaturity() {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();
        LocalDate oneYearLater = currentDate.plusYears(1);
        String oneYearLaterStr = oneYearLater.format(FORMATTER);
        Assertions.assertTrue(
                areDoublesEqual(FinancialUtil.yearsToMaturity(oneYearLaterStr), 1.0));
    }

    private static boolean areDoublesEqual(double a, double b) {
        return Math.abs(a - b) < 1e-3;
    }
}
