package com.exchange.system.pricing;

public final class StandardNormalCDF {

    /**
     * P(Z < x)
     */
    public static double cumulativeProbability(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("Input must be a finite number");
        }
        if (x < 0) {
            return 1.0 - cumulativeProbability(-x);
        }
        // Using mathematical approximation
        double COEFFICIENT_B0 = 0.2316419;
        double b1 = 0.319381530;
        double b2 = -0.356563782;
        double b3 = 1.781477937;
        double b4 = -1.821255978;
        double b5 = 1.330274429;

        double term = 1.0 / (1.0 + COEFFICIENT_B0 * x);
        double termSquared = term * term;
        double t3 = termSquared * term;
        double t4 = t3 * term;
        double t5 = t4 * term;

        double pdf = Math.exp(-x * x / 2.0) / Math.sqrt(2.0 * Math.PI);

        double result = 1.0 - pdf * (b1 * term + b2 * termSquared + b3 * t3 + b4 * t4 + b5 * t5);

        return result;
    }

}
