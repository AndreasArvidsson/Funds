package com.github.andreasarvidsson.founds;

import com.github.andreasarvidsson.founds.util.Pair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    public final List<FoundRank> founds = new ArrayList();
    public final List<String> headers = new ArrayList();

    public Rankings(final String name, final String... foundNames) throws IOException {
        this(name, false, foundNames);
    }

    public Rankings(final String name, final boolean useSavr, final String... foundNames) throws IOException {
        this.name = name;
        for (final String foundName : foundNames) {
            final FoundRank fr = new FoundRank(
                    Avanza.getFound(foundName),
                    useSavr ? SAVR.getFound(foundName) : null
            );
            founds.add(fr);
        }
        final List<Pair<FoundRank, Double>> fees = getFees();
        addValues("Avgift", fees);
        final List<Pair<FoundRank, Double>> sharpeRatio = getSharpeRatio();
        if (sharpeRatio != null) {
            addValues(Headers.SHARPE_RATIO, sharpeRatio);
        }
        for (int i = 0; i < 4; ++i) {
            final List<Pair<FoundRank, Double>> values = getValues(i);
            if (values == null) {
                break;
            }
            addValues(DEV_HEADERS.get(i), values);
        }
        Collections.sort(founds, (a, b) -> Integer.compare(b.points, a.points));
    }

    private void addValues(final String header, final List<Pair<FoundRank, Double>> values) {
        headers.add(header);
        for (int j = 0; j < values.size(); ++j) {
            final Pair<FoundRank, Double> p = values.get(j);
            final FoundRank fr = p.first();
            final int points = j + 1;
            fr.points += points;
            fr.values.add(new Pair(points, p.second()));
        }
    }

    private List<Pair<FoundRank, Double>> getFees() {
        final List<Pair<FoundRank, Double>> res = new ArrayList();
        founds.forEach(found -> {
            res.add(new Pair(found, found.getFee()));
        });
        Collections.sort(res, (a, b) -> Double.compare(b.second(), a.second()));
        return res;
    }

    private List<Pair<FoundRank, Double>> getSharpeRatio() {
        final List<Pair<FoundRank, Double>> res = new ArrayList();
        for (final FoundRank found : founds) {
            if (found.avanza.sharpeRatio == null) {
                return null;
            }
            res.add(new Pair(found, found.avanza.sharpeRatio));
        }
        Collections.sort(res, (a, b) -> Double.compare(a.second(), b.second()));
        return res;
    }

    private List<Pair<FoundRank, Double>> getValues(final int level) {
        final List<Pair<FoundRank, Double>> res = new ArrayList();
        for (final FoundRank found : founds) {
            final Double value = getValue(found.avanza, level);
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

        public final AvanzaFound avanza;
        public final SavrFound savr;
        public int points = 0;
        public final List<Pair<Integer, Double>> values = new ArrayList();

        public FoundRank(final AvanzaFound avanza, final SavrFound savr) {
            this.avanza = avanza;
            this.savr = savr;
        }

        public double getFee() {
            return savr != null ? savr.fee : avanza.productFee;
        }

    }

}
