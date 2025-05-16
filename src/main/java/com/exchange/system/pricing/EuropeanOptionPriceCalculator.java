package com.exchange.system.pricing;

import static com.exchange.system.constants.ExchangeSystemConstants.RISK_FREE_RATE;

public class EuropeanOptionPriceCalculator {

    private static double calculateD1(double spotPrice
            , double strikePrice
            , double volatility
            , double timeToMaturity) {
        return (Math.log(spotPrice / strikePrice)
                + (RISK_FREE_RATE + (volatility * volatility) / 2) * timeToMaturity)
                / (volatility * Math.sqrt(timeToMaturity));
    }

    private static double calculateD2(double spotPrice
            , double strikePrice
            , double volatility
            , double timeToMaturity) {
        return calculateD1(spotPrice, strikePrice, volatility, timeToMaturity)
                - volatility * Math.sqrt(timeToMaturity);
    }


    public static double calculateCallPrice(double spotPrice
            , double strikePrice
            , double volatility
            , String maturity) {
        double t = FinancialUtil.yearsToMaturity(maturity);
        double d1 = calculateD1(spotPrice, strikePrice, volatility, t);
        double d2 = calculateD2(spotPrice, strikePrice, volatility, t);

        return spotPrice * StandardNormalCDF.cumulativeProbability(d1)
                - strikePrice * Math.exp(-1 * RISK_FREE_RATE * t) * StandardNormalCDF.cumulativeProbability(d2);
    }

    public static double calculatePutPrice(double spotPrice
            , double strikePrice
            , double volatility
            , String maturity) {
        double t = FinancialUtil.yearsToMaturity(maturity);
        double d1 = calculateD1(spotPrice, strikePrice, volatility, t);
        double d2 = calculateD2(spotPrice, strikePrice, volatility, t);
        return strikePrice * Math.exp(-1 * RISK_FREE_RATE * t) * StandardNormalCDF.cumulativeProbability(-1 * d2)
                - spotPrice * StandardNormalCDF.cumulativeProbability(-1 * d1);
    }
}
