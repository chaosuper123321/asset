package com.exchange.system.provider;


import com.exchange.system.constants.Constants;
import com.exchange.system.utils.StandardNormalRandom;

public class StockPriceCalc {
    public static final long CYCLE_TIME = Constants.ProviderSchedulePeriodSeconds;

    public static double calcNewPrice(double S, double u, double q) {
        double e = StandardNormalRandom.nextStandardNormal();
        double t = divideByCycle(CYCLE_TIME);
        double delta = u * t + q * e * Math.sqrt(t);
        return S * (1 + delta);
    }

    public static double divideByCycle(long seconds) {
        return (double) seconds / Constants.CYCLE_SECONDS;
    }

}
