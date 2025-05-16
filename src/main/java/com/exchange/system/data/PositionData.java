package com.exchange.system.data;

import java.util.HashMap;
import java.util.TreeMap;

public class PositionData {

    private HashMap<String, Double> positions = new HashMap<>();

    private static class SingletonHolder {

        private static final PositionData INSTANCE = new PositionData();
    }

    private PositionData() {
    }

    public static PositionData getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public HashMap<String, Double> getPositions() {
        return positions;
    }

    public void updatePositions(TreeMap<String, Double> newPositions) {
        positions.putAll(newPositions);
    }

}
