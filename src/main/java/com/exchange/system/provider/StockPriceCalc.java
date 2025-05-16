package com.exchange.system.provider;


import com.exchange.system.constants.Constants;
import com.exchange.system.utils.StandardNormalRandom;

public class StockPriceCalc {
    public static final long CYCLE_TIME = Constants.PROVIDER_SCHEDULE_PERIOD_SECONDS;

    public static double calcNewPrice(double curPrice
            , double expectedReturn
            , double annualizedStandardDeviation) {
        double e = StandardNormalRandom.nextStandardNormal();
        double t = divideByCycle(CYCLE_TIME);
        double delta = expectedReturn * t + annualizedStandardDeviation * e * Math.sqrt(t);
        return curPrice * (1 + delta);
    }

    public static double divideByCycle(long seconds) {
        return (double) seconds / Constants.CYCLE_SECONDS;
    }
}
