package com.exchange.system.data.csv;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class PositionCsvParser {

    public static final String POSITION_FILE_NAME = "positions.csv";

    public static TreeMap<String, Double> loadPositions() {
        TreeMap<String, Double> positions = new TreeMap<>();
        List<String[]> rows = new ArrayList<>();
        ClassLoader classLoader = PositionCsvParser.class.getClassLoader();

        try (InputStream input = classLoader.getResourceAsStream(POSITION_FILE_NAME);
                BufferedReader reader = input != null ?
                        new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))
                        : null) {
            if (input == null || reader == null) {
                System.err.println("Cannot find " + POSITION_FILE_NAME);
                return null;
            }
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length == 2) {
                    rows.add(row);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            System.err.println("read " + POSITION_FILE_NAME + " failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        for (String[] row : rows) {
            positions.put(row[0], Double.parseDouble(row[1]));
        }
        return positions;
    }
}
