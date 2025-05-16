package com.exchange.system.pricing;


import com.exchange.system.constants.ExchangeSystemConstants;

public class SecurityPriceCalculator {

    public static final long PRICE_UPDATE_PERIOD_SECONDS
            = ExchangeSystemConstants.PROVIDER_SCHEDULE_PERIOD_SECONDS;

    public static double calculateUpdatedPrice(double currentPrice
            , double expectedReturn
            , double annualizedStandardDeviation) {
        double randomFactor = StandardNormalRandom.nextStandardNormal();
        double timeFraction = calculateTimeFraction(PRICE_UPDATE_PERIOD_SECONDS);
        double delta = expectedReturn * timeFraction
                + annualizedStandardDeviation * randomFactor * Math.sqrt(timeFraction);
        return currentPrice * (1 + delta);
    }

    public static double calculateTimeFraction(long seconds) {
        return (double) seconds / ExchangeSystemConstants.MARKET_DATA_CYCLE_SECONDS;
    }
}
