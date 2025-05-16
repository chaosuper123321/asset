package com.exchange.system.pricing;

import java.security.SecureRandom;

public final class StandardNormalRandom {

    private static final SecureRandom random = new SecureRandom();
    private static final ThreadLocal<RandomState> STATE
            = ThreadLocal.withInitial(RandomState::new);
    private static class RandomState {
        boolean hasSpareRandom = false;
        double spareRandom;
    }

    public static double nextStandardNormal() {
        RandomState state = STATE.get();
        if (state.hasSpareRandom) {
            state.hasSpareRandom = false;
            return state.spareRandom;
        }

        double xCoordinate, yCoordinate, radiusSquared;
        do {
            xCoordinate = 2.0 * random.nextDouble() - 1.0;
            yCoordinate = 2.0 * random.nextDouble() - 1.0;
            radiusSquared = xCoordinate * xCoordinate + yCoordinate * yCoordinate;
        } while (radiusSquared >= 1.0 || radiusSquared == 0.0);

        double multiplier = Math.sqrt(-2.0 * Math.log(radiusSquared) / radiusSquared);
        state.spareRandom = yCoordinate * multiplier;
        state.hasSpareRandom = true;

        return xCoordinate * multiplier;
    }

}
