package com.github.andreasarvidsson.founds.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andreas Arvidsson
 */
public class Sum {

    private final Map<String, Double> sum = new HashMap();
    private final Map<String, Double> percentages = new HashMap();

    public void add(final String key, final double value, final double percentage) {
        if (!sum.containsKey(key)) {
            sum.put(key, 0.0);
            percentages.put(key, 0.0);
        }
        sum.put(key, sum.get(key) + value * percentage);
        percentages.put(key, percentages.get(key) + percentage);
    }

    public void normalize() {
        sum.entrySet().forEach(e -> {
            sum.put(e.getKey(), e.getValue() / percentages.get(e.getKey()));
        });
    }

    public boolean has(final String key) {
        return sum.containsKey(key);
    }

    public double get(final String key) {
        return sum.get(key);
    }

}
