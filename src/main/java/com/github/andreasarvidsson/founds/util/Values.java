package com.github.andreasarvidsson.founds.util;

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

    public void add(final String key, final Double value) {
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

    public double get(final String key) {
        return map.get(key);
    }

    public Pair<String, Double> get(final int index) {
        return list.get(index);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }

    @Override
    public Iterator<Pair<String, Double>> iterator() {
        return list.iterator();
    }

}