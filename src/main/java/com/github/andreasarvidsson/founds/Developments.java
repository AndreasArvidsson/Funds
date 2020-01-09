package com.github.andreasarvidsson.founds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Andreas Arvidsson
 */
public class Developments {

    public static final String T_6_M = "6 m";
    public static final String T_1_Y = "1 år";
    public static final String T_3_Y = "3 år";
    public static final String T_5_Y = "5 år";

    public static final List<String> DEVELOPMENT_TITLES = Arrays.asList(
            "1 d", "1 m", "3 m", T_6_M, "i år", T_1_Y, T_3_Y, T_5_Y
    );

    private final Map<String, Integer> counts = new HashMap();
    private final Map<String, Double> values = new HashMap();

    public Developments(final List<Pair<Found, Double>> founds) {
        double percentageSum = 0.0;
        for (final Pair<Found, Double> foundPair : founds) {
            final Found found = foundPair.first();
            final double percentage = foundPair.second();
            percentageSum += percentage;
            getDevelopment(found).forEach(d -> {
                add(d.first(), d.second() * percentage);
            });
        }
        normalize(founds.size(), percentageSum);
    }

    public boolean has(final String key) {
        return counts.containsKey(key);
    }

    public Double get(final String key) {
        return values.get(key);
    }

    public static List<Pair<String, Double>> getDevelopment(final Found found) {
        final List<Pair<String, Double>> res = new ArrayList();
        final Iterator<String> it = DEVELOPMENT_TITLES.iterator();
        if (found.developmentOneDay != null) {
            res.add(new Pair(it.next(), found.developmentOneDay));
        }
        if (found.developmentOneMonth != null) {
            res.add(new Pair(it.next(), found.developmentOneMonth));
        }
        if (found.developmentThreeMonths != null) {
            res.add(new Pair(it.next(), found.developmentThreeMonths));
        }
        if (found.developmentSixMonths != null) {
            res.add(new Pair(it.next(), found.developmentSixMonths));
        }
        if (found.developmentThisYear != null) {
            res.add(new Pair(it.next(), found.developmentThisYear));
        }
        if (found.developmentOneYear != null) {
            res.add(new Pair(it.next(), found.developmentOneYear));
        }
        if (found.developmentThreeYears != null) {
            res.add(new Pair(it.next(), found.developmentThreeYears));
        }
        if (found.developmentFiveYears != null) {
            res.add(new Pair(it.next(), found.developmentFiveYears));
        }
        return res;
    }

    private void add(final String key, final Double value) {
        if (!counts.containsKey(key)) {
            counts.put(key, 0);
            values.put(key, 0.0);
        }
        counts.put(key, counts.get(key) + 1);
        values.put(key, values.get(key) + value);
    }

    private void normalize(final int expectedCount, final double percentage) {
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

}
