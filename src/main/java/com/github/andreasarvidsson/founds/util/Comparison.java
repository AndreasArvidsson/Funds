package com.github.andreasarvidsson.founds.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public class Comparison implements Iterable<String> {

    private final Set<String> keys = new HashSet();
    private final Map<String, Double> firstMap = new HashMap();
    private final Map<String, Double> secondMap = new HashMap();
    private final Map<String, Double> diffMap = new HashMap();
    private final List<String> order = new ArrayList();

    public void putFirst(final String key, final double value) {
        keys.add(key);
        firstMap.put(key, value);
    }

    public void putSecond(final String key, final double value) {
        keys.add(key);
        secondMap.put(key, value);
    }

    public String get(final int index) {
        return order.get(index);
    }

    public double first(final String key) {
        if (firstMap.containsKey(key)) {
            return firstMap.get(key);
        }
        return 0.0;
    }

    public double second(final String key) {
        if (secondMap.containsKey(key)) {
            return secondMap.get(key);
        }
        return 0.0;
    }

    public double diff(final String key) {
        if (diffMap.containsKey(key)) {
            return diffMap.get(key);
        }
        return 0.0;
    }

    public void compile() {
        keys.forEach(key -> {
            order.add(key);
            diffMap.put(key, second(key) - first(key));
        });
        Collections.sort(order, (a, b)
                -> Double.compare(Math.abs(diff(b)), Math.abs(diff(a)))
        );
    }

    public boolean isEmpty() {
        return order.isEmpty();
    }

    public int size() {
        return order.size();
    }

    @Override
    public Iterator<String> iterator() {
        return order.iterator();
    }

}
