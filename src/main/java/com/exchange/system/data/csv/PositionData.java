package com.exchange.system.data.csv;

import java.util.Collections;
import java.util.TreeMap;

public class PositionData {
    private TreeMap<String, Double> data = new TreeMap<>();

    private static class SingletonHolder {
        private static final PositionData INSTANCE = new PositionData();
    }
    private PositionData() {
    }

    public static PositionData getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public TreeMap<String, Double> getData() {
        return data;
    }

    public void setData(TreeMap<String, Double> newData) {
        data.putAll(newData);
    }

}
