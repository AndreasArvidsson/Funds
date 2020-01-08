package com.github.andreasarvidsson.founds;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Andreas Arvidsson
 */
public class Developments {

    private final Map<String, Integer> counts = new HashMap();
    private final Map<String, Double> values = new HashMap();

    public void add(final String key, final Double value) {
        if (!counts.containsKey(key)) {
            counts.put(key, 0);
            values.put(key, 0.0);
        }
        counts.put(key, counts.get(key) + 1);
        values.put(key, values.get(key) + value);
    }

    public void normalize(final int expectedCount, final double percentage) {
        final Iterator<Entry<String, Integer>> it = counts.entrySet().iterator();
        while (it.hasNext()) {
            final Entry<String, Integer> e = it.next();
            if (e.getValue() != expectedCount) {
                it.remove();
                values.remove(e.getKey());
            }
            else {
                values.put(e.getKey(), values.get(e.getKey()) / percentage);
            }
        }
    }

    public boolean has(final String key) {
        return counts.containsKey(key);
    }

    public Double get(final String key) {
        return values.get(key);
    }

}
