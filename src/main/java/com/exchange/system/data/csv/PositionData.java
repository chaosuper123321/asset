package com.exchange.system.data.csv;

import java.util.TreeMap;

public class PositionData {
    private static PositionData instance = new PositionData();
    private PositionData() { }
    public static PositionData getInstance() {
        return instance;
    }

    public TreeMap<String, Float> getData() {
        return data;
    }

    public void setData(TreeMap<String, Float> data) {
        this.data = data;
    }

    private TreeMap<String, Float> data;
}
