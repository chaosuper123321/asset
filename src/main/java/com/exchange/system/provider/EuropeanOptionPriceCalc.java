package com.exchange.system.provider;

import com.exchange.system.utils.PriceUtil;
import com.exchange.system.utils.StandardNormalCDF;

public class EuropeanOptionPriceCalc {
    public static final double R = 0.02;

    public static double calcD1(double spotPrice
            , double strikePrice
            , double volatility
            , double timeToMaturity) {
        double d1 = (Math.log(spotPrice / strikePrice) + (R + (volatility * volatility) / 2) * timeToMaturity)
                        / (volatility * Math.sqrt(timeToMaturity));
        return d1;
    }

    public static double calcD2(double spotPrice
            , double strikePrice
            , double volatility
            , double timeToMaturity) {
        double d2 = calcD1(spotPrice, strikePrice, volatility, timeToMaturity)
                - volatility * Math.sqrt(timeToMaturity);
        return d2;
    }


    public static double calcCallPrice(double spotPrice
            , double strikePrice
            , double volatility
            , String maturity) {
        double t = PriceUtil.calculateYearsToMaturity(maturity);
        double d1 = calcD1(spotPrice, strikePrice, volatility, t);
        double d2 = calcD2(spotPrice, strikePrice, volatility, t);

        double c =
                spotPrice * StandardNormalCDF.N(d1)
                        - strikePrice * Math.exp(-1 * R * t) * StandardNormalCDF.N(d2);
        return c;
    }

    public static double calcPutPrice(double spotPrice
            , double strikePrice
            , double volatility
            , String maturity) {
        double t = PriceUtil.calculateYearsToMaturity(maturity);
        double d1 = calcD1(spotPrice, strikePrice, volatility, t);
        double d2 = calcD2(spotPrice, strikePrice, volatility, t);
        double p = strikePrice * Math.exp(-1 * R * t) * StandardNormalCDF.N(-1 * d2)
                - spotPrice * StandardNormalCDF.N(-1 * d1);
        return p;
    }
}
