package com.exchange.system.utils;

import java.security.SecureRandom;

public class StandardNormalRandom {
    private static final SecureRandom random = new SecureRandom();
    private static boolean hasSpare = false;
    private static double spare;

    public static double nextStandardNormal() {
        if (hasSpare) {
            hasSpare = false;
            return spare;
        }

        double u1, u2, s;
        do {
            u1 = random.nextDouble();
            u2 = random.nextDouble();
            u1 = 2.0 * u1 - 1.0;
            u2 = 2.0 * u2 - 1.0;
            s = u1 * u1 + u2 * u2;
        } while (s >= 1.0 || s == 0.0);

        double multiplier = Math.sqrt(-2.0 * Math.log(s) / s);
        spare = u2 * multiplier;
        hasSpare = true;

        return u1 * multiplier;
    }

}
