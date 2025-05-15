package com.exchange.system.utils;

public class StandardNormalCDF {
    /**
     *  P(Z < x)
     */
    public static double N(double x) {
        if (x < 0) {
            return 1.0 - N(-x);
        }

        double b0 = 0.2316419;
        double b1 = 0.319381530;
        double b2 = -0.356563782;
        double b3 = 1.781477937;
        double b4 = -1.821255978;
        double b5 = 1.330274429;

        double t = 1.0 / (1.0 + b0 * x);
        double t2 = t * t;
        double t3 = t2 * t;
        double t4 = t3 * t;
        double t5 = t4 * t;

        double pdf = Math.exp(-x * x / 2.0) / Math.sqrt(2.0 * Math.PI);

        double result = 1.0 - pdf * (b1 * t + b2 * t2 + b3 * t3 + b4 * t4 + b5 * t5);

        return result;
    }

}
