package com.github.andreasarvidsson.funds.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andreas Arvidsson
 */
public class Values implements Iterable<Pair<String, Double>> {

    private final List<String> insertOrder = new ArrayList();
    private final List<Pair<String, Double>> list = new ArrayList();
    private final Map<String, Double> map = new HashMap();

    public void add(final String key, final double value) {
        if (!map.containsKey(key)) {
            map.put(key, 0.0);
            insertOrder.add(key);
        }
        map.put(key, map.get(key) + value);
    }

    public void compile() {
        compile(false);
    }

    public void compile(final boolean sort) {
        if (sort) {
            map.entrySet().forEach(e -> {
                list.add(new Pair(e.getKey(), e.getValue()));
            });
            Collections.sort(list, (a, b) -> Double.compare(b.second(), a.second()));
        }
        else {
            insertOrder.forEach(key -> {
                list.add(new Pair(key, map.get(key)));
            });
        }
    }

    public boolean has(final String key) {
        return map.containsKey(key);
    }

    public boolean has(final String... keys) {
        for (final String key : keys) {
            if (has(key)) {
                return true;
            }
        }
        return false;
    }

    public double get(final String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return 0;
    }

    public double get(final String... keys) {
        double res = 0;
        for (final String key : keys) {
            res += get(key);
        }
        return res;
    }

    public Pair<String, Double> get(final int index) {
        return list.get(index);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public int size() {
        return map.size();
    }

    public void normalize(final String key, final double sum) {
        map.put(key, map.get(key) / sum);
    }

    @Override
    public Iterator<Pair<String, Double>> iterator() {
        return list.iterator();
    }

}
