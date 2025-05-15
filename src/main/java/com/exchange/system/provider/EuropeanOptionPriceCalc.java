package com.exchange.system.provider;


import com.exchange.system.constants.Constants;
import com.exchange.system.utils.PriceUtil;
import com.exchange.system.utils.StandardNormalCDF;

public class EuropeanOptionPriceCalc {
    public static final double R = 0.02;
    public static final long CYCLE_TIME = Constants.ProviderSchedulePeriodSeconds;

    public static double calcD1(double s, double k, double q, double t) {
        double d1 =
                (Math.log(s / k) + (R + (q * q) / 2) * t) / (q * Math.sqrt(t));
        return d1;
    }

    public static double calcD2(double s, double k, double q, double t) {
        double d2 = calcD1(s, k, q, t) - q * Math.sqrt(t);
        return d2;
    }

    public static double calcC(double s, double k, double q, String maturity) {
        double t = PriceUtil.calculateYearsToMaturity(maturity);
        double d1 = calcD1(s, k, q, t);
        double d2 = calcD2(s, k, q, t);

        double c =
                s * StandardNormalCDF.N(d1) - k * Math.exp(-1 * R * CYCLE_TIME) * StandardNormalCDF
                        .N(d2);
        return c;
    }

    public static double calcP(double s, double k, double q, String maturity) {
        double t = PriceUtil.calculateYearsToMaturity(maturity);
        double d1 = calcD1(s, k, q, t);
        double d2 = calcD2(s, k, q, t);
        double p = k * Math.exp(-1 * R * CYCLE_TIME) * StandardNormalCDF.N(-1 * d2)
                - s * StandardNormalCDF.N(-1 * d1);
        return p;
    }
}
