package com.exchange.system.application;


import com.exchange.system.data.csv.PositionCsvParser;
import java.util.TreeMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PositionCsvParserTest {

    @Test
    public void testGetPositionDataFromCSV() {
        TreeMap<String, Double> data =  PositionCsvParser.loadPositions();
        Assertions.assertTrue(data != null);
    }
}
