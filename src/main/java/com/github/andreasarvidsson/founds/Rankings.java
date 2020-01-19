package com.github.andreasarvidsson.founds;

import com.github.andreasarvidsson.founds.util.Pair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andreas Arvidsson
 */
public class Rankings {

    private final static List<String> DEV_HEADERS = Arrays.asList(
            Headers.T_6_M,
            Headers.T_1_Y,
            Headers.T_3_Y,
            Headers.T_5_Y
    );

    public final String name;
    public final List<FoundRank> list = new ArrayList();
    public final List<String> headers = new ArrayList();
    private final Map<AvanzaFound, FoundRank> map = new HashMap();

    public Rankings(final String name, final String... foundNames) throws IOException {
        this(name, Arrays.asList(foundNames));
    }

    public Rankings(final String name, final List<String> foundNames) throws IOException {
        this.name = name;
        final List<AvanzaFound> founds = new ArrayList();
        for (final String foundName : foundNames) {
            final AvanzaFound found = Avanza.getFound(foundName);
            final FoundRank fr = new FoundRank(found);
            list.add(fr);
            map.put(fr.found, fr);
            founds.add(found);
        }
        final List<Pair<AvanzaFound, Double>> fees = getFees(founds);
        addValues("Avgift", fees);
        final List<Pair<AvanzaFound, Double>> sharpeRatio = getSharpeRatio(founds);
        if (sharpeRatio != null) {
            addValues(Headers.SHARP_RATIO, sharpeRatio);
        }
        for (int i = 0; i < 4; ++i) {
            final List<Pair<AvanzaFound, Double>> values = getValues(founds, i);
            if (values == null) {
                break;
            }
            addValues(DEV_HEADERS.get(i), values);
        }
        Collections.sort(list, (a, b) -> Integer.compare(b.points, a.points));
    }

    private void addValues(final String header, final List<Pair<AvanzaFound, Double>> values) {
        headers.add(header);
        for (int j = 0; j < values.size(); ++j) {
            final Pair<AvanzaFound, Double> p = values.get(j);
            final FoundRank fr = map.get(p.first());
            final int points = j + 1;
            fr.points += points;
            fr.values.add(new Pair(points, p.second()));
        }
    }

    private List<Pair<AvanzaFound, Double>> getFees(
            final List<AvanzaFound> founds) {
        final List<Pair<AvanzaFound, Double>> res = new ArrayList();
        for (final AvanzaFound found : founds) {
            res.add(new Pair(found, found.productFee));
        }
        Collections.sort(res, (a, b) -> Double.compare(b.second(), a.second()));
        return res;
    }

    private List<Pair<AvanzaFound, Double>> getSharpeRatio(
            final List<AvanzaFound> founds) {
        final List<Pair<AvanzaFound, Double>> res = new ArrayList();
        for (final AvanzaFound found : founds) {
            if (found.sharpeRatio == null) {
                return null;
            }
            res.add(new Pair(found, found.sharpeRatio));
        }
        Collections.sort(res, (a, b) -> Double.compare(a.second(), b.second()));
        return res;
    }

    private List<Pair<AvanzaFound, Double>> getValues(
            final List<AvanzaFound> founds,
            final int level) {
        final List<Pair<AvanzaFound, Double>> res = new ArrayList();
        for (final AvanzaFound found : founds) {
            final Double value = getValue(found, level);
            if (value == null) {
                return null;
            }
            res.add(new Pair(found, value));
        }
        Collections.sort(res, (a, b) -> Double.compare(a.second(), b.second()));
        return res;
    }

    private Double getValue(final AvanzaFound found, final int level) {
        switch (level) {
            case 0:
                return found.developmentSixMonths;
            case 1:
                return found.developmentOneYear;
            case 2:
                return found.developmentThreeYears;
            case 3:
                return found.developmentFiveYears;
            default:
                return null;
        }
    }

    public static class FoundRank {

        public final AvanzaFound found;
        public int points = 0;
        public final List<Pair<Integer, Double>> values = new ArrayList();

        public FoundRank(final AvanzaFound found) {
            this.found = found;
        }

    }

}
