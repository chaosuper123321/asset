package com.exchange.system.pricing;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class FinancialUtil {

    public static final double PRICE_COMPARISON_TOLERANCE = 0.01;

    public static boolean areEqual(double price1, double price2, double epsilon) {
        return Math.abs(price1 - price2) < epsilon;
    }

    public static double yearsToMaturity(String maturity) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate maturityDate = LocalDate.parse(maturity, formatter);
            LocalDate currentDate = LocalDate.now();
            long days = ChronoUnit.DAYS.between(currentDate, maturityDate);
            double years = days / 365.25;
            return years;
        } catch (Exception e) {
            e.printStackTrace();
            return -1.0;
        }
    }


}
