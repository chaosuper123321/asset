package com.exchange.system.utils;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class PriceUtil {
    public static final double EPSILON = 1e-2;

    public static boolean areEqual(double a, double b, double epsilon) {
        return Math.abs(a - b) < epsilon;
    }

    public static double calculateYearsToMaturity(String maturity) {
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
