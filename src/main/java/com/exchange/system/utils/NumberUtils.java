package com.exchange.system.utils;


public class NumberUtils {
    public static double roundToDecimalPlaces(double value, int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Decimal places cannot be negative");
        }
        double scale = Math.pow(10.0, decimalPlaces);
        return Math.round(value * scale) / scale;
    }
}
