package com.github.andreasarvidsson.founds;

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

    private final static List<String> HEADERS = Arrays.asList(
            "Avgift",
            Developments.T_6_M,
            Developments.T_1_Y,
            Developments.T_3_Y,
            Developments.T_5_Y
    );

    private final List<FoundRank> list = new ArrayList();
    private final Map<Found, FoundRank> map = new HashMap();

    public static Rankings create(final List<String> foundNames) throws IOException {
        return new Rankings(
                Avanza.getFounds(foundNames)
        );
    }

    public Rankings(final List<Found> founds) {
        founds.forEach(found -> {
            final FoundRank fr = new FoundRank(found);
            list.add(fr);
            map.put(fr.found, fr);
        });
        final List<Pair<Found, Double>> fees = getFees(founds);
        addValues(fees);
        for (int i = 0; i < 4; ++i) {
            final List<Pair<Found, Double>> values = getValues(founds, i);
            if (values == null) {
                break;
            }
            addValues(values);
        }
        Collections.sort(list, (a, b) -> Integer.compare(b.points, a.points));
    }

    private void addValues(final List<Pair<Found, Double>> values) {
        for (int j = 0; j < values.size(); ++j) {
            final Pair<Found, Double> p = values.get(j);
            final FoundRank fr = map.get(p.first());
            final int points = j + 1;
            fr.points += points;
            fr.values.add(new Pair(points, p.second()));
        }
    }

    public void print() {
        System.out.printf(
                "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                + " Ranking "
                + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n"
        );
        final Table table = new Table();
        final List<String> headers = new ArrayList();
        headers.add("");
        headers.addAll(HEADERS);
        headers.add("Poäng");
        table.addRow(headers);
        table.addHR();
        list.forEach(fr -> {
            final List<String> row = new ArrayList();
            row.add(fr.found.name);
            for (int i = 0; i < HEADERS.size(); ++i) {
                if (i < fr.values.size()) {
                    final Pair<Integer, Double> p = fr.values.get(i);
                    row.add(String.format("%d (%.2f)", p.first(), p.second()));
                }
                else {
                    row.add("");
                }
            }
            row.add(String.format("%d", fr.points));
            table.addRow(row);
        });
        table.print();
    }

    private List<Pair<Found, Double>> getFees(
            final List<Found> founds) {
        final List<Pair<Found, Double>> res = new ArrayList();
        for (final Found found : founds) {
            res.add(new Pair(found, found.productFee));
        }
        Collections.sort(res, (a, b) -> Double.compare(b.second(), a.second()));
        return res;
    }

    private List<Pair<Found, Double>> getValues(
            final List<Found> founds,
            final int level) {
        final List<Pair<Found, Double>> res = new ArrayList();
        for (final Found found : founds) {
            final Double value = getValue(found, level);
            if (value == null) {
                return null;
            }
            res.add(new Pair(found, value));
        }
        Collections.sort(res, (a, b) -> Double.compare(a.second(), b.second()));
        return res;
    }

    private Double getValue(final Found found, final int level) {
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

}

class FoundRank {

    public final Found found;
    public int points = 0;
    public final List<Pair<Integer, Double>> values = new ArrayList();

    public FoundRank(final Found found) {
        this.found = found;
    }

}
