package com.exchange.system.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class CsvUtil {

    public static final String FILE_NAME = "positionfile.csv";

    public static TreeMap<String, Float> getPositionDataFromCSV() {
        TreeMap<String, Float> ret = new TreeMap<>();
        List<String[]> data = new ArrayList<>();
        ClassLoader classLoader = CsvUtil.class.getClassLoader();

        try (InputStream input = classLoader.getResourceAsStream(FILE_NAME);
                BufferedReader reader = input != null ?
                        new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))
                        : null) {
            if (input == null || reader == null) {
                System.err.println("can not find " + FILE_NAME);
                return null;
            }
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length==2) {
                    data.add(row);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            System.err.println("read " + FILE_NAME + " failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        for (String[] row : data) {
            ret.put(row[0], Float.parseFloat(row[1]));
        }
        return ret;
    }
}
