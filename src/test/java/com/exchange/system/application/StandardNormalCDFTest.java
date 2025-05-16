package com.exchange.system.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.exchange.system.pricing.StandardNormalCDF;
import org.junit.jupiter.api.Test;

public class StandardNormalCDFTest {

    @Test
    public void testN() {
        Double n1 = StandardNormalCDF.cumulativeProbability(0.5);
        Double n2 = StandardNormalCDF.cumulativeProbability(-0.5);
        assertTrue((n1 + n2) == 1);
    }

    @Test
    public void testN1() {
        assertEquals(0.5, StandardNormalCDF.cumulativeProbability(0.0), 1e-6);
        assertEquals(1.0, StandardNormalCDF.cumulativeProbability(10.0), 1e-6);
        assertThrows(IllegalArgumentException.class,
                () -> StandardNormalCDF.cumulativeProbability(Double.NaN)
        );
    }
}
