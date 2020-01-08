package com.mycompany.test;

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
public class PortfolioSummary {

    public final String name;
    public double percentageSum, avgFee;
    public final List<Pair<Found, Double>> founds;
    public List<Pair<String, Double>> countries, sectors, regions;
    public final Developments developments = new Developments();

    public PortfolioSummary(
            final String name,
            final List<Pair<Found, Double>> founds) throws IOException {
        this.name = name;
        this.founds = founds;
        final Map<String, Double> countriesMap = new HashMap();
        final Map<String, Double> sectorsMap = new HashMap();
        final Map<String, Double> regionsMap = new HashMap();
        for (final Pair<Found, Double> foundPair : founds) {
            final Found found = foundPair.first();
            final double percentage = foundPair.second();
            percentageSum += percentage;
            avgFee += found.productFee * percentage;
            found.countryChartData.forEach(data -> {
                addToMap(countriesMap, data.name, data.y * percentage);
                addToMap(regionsMap, Regions.get(data.name), data.y * percentage);
            });
            found.sectorChartData.forEach(data -> {
                addToMap(sectorsMap, data.name, data.y * percentage);
            });
            found.getDevelopment().forEach(d -> {
                developments.add(d.first(), d.second() * percentage);
            });
        }
        avgFee /= percentageSum;
        countries = normalizeMap(countriesMap);
        sectors = normalizeMap(sectorsMap);
        regions = normalizeMap(regionsMap);
        developments.normalize(founds.size(), percentageSum);
        percentageSum *= 100;
    }

    public void print() {
        System.out.printf(
                "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< %s >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n",
                name
        );
        final Table table = new Table();

        final List<String> titles = new ArrayList();
        titles.addAll(Arrays.asList("Namn", "Andel (%)", "Avgift (%)", "Kategorier"));
        titles.addAll(Found.DEVELOPMENT_TITLES);
        table.addRow(titles);
        table.addHR();

        for (final Pair<Found, Double> foundPair : founds) {
            final Found found = foundPair.first();
            final double percentage = foundPair.second();
            final List<String> row = new ArrayList();
            row.addAll(Arrays.asList(
                    found.name,
                    format(percentage * 100),
                    format(found.productFee),
                    String.join(", ", found.categories)
            ));
            found.getDevelopment().forEach(d -> {
                row.add(format(d.second()));
            });
            table.addRow(row);
        }

        table.addHR();

        final List<String> row = new ArrayList();
        row.addAll(Arrays.asList(
                "",
                format(percentageSum),
                format(avgFee),
                ""
        ));
        Found.DEVELOPMENT_TITLES.forEach(title -> {
            if (developments.has(title)) {
                row.add(format(developments.get(title)));
            }
        });
        table.addRow(row);

        table.print();

        table.reset();
        final String space = "   |   ";
        table.addRow("Land", "Andel (%)", space, "Region", "Andel (%)", space, "Bransch", "Andel (%)");
        table.addHR();
        for (int i = 0; i < 10; ++i) {
            table.addRow(Arrays.asList(
                    i < countries.size() ? countries.get(i).first() : "",
                    i < countries.size() ? format(countries.get(i).second()) : "",
                    space,
                    i < regions.size() ? regions.get(i).first() : "",
                    i < regions.size() ? format(regions.get(i).second()) : "",
                    space,
                    i < sectors.size() ? sectors.get(i).first() : "",
                    i < sectors.size() ? format(sectors.get(i).second()) : ""
            ));
        }
        table.print();
    }

    public void compare(final PortfolioSummary summary) {
        System.out.printf(
                "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< %s vs %s >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n",
                name, summary.name
        );
        final Table table = new Table();
        table.addRow("", name, summary.name, "Skillnad");
        table.addHR();
        table.addRow(
                "Andel (%)",
                format(percentageSum),
                format(summary.percentageSum),
                format(summary.percentageSum - percentageSum)
        );
        table.addRow(
                "Avgift (%)",
                format(avgFee),
                format(summary.avgFee),
                format(summary.avgFee - avgFee)
        );
        table.print();
        compareMaps("Land", name, summary.name, countries, summary.countries);
        compareMaps("Region", name, summary.name, regions, summary.regions);
        compareMaps("Bransch", name, summary.name, sectors, summary.sectors);
        compareDevelopments(summary);
    }

    private void compareDevelopments(final PortfolioSummary summary) {
        final Table table = new Table();
        table.addRow(
                "Utveckling (%)",
                String.format("%s (%%)", name),
                String.format("%s (%%)", summary.name),
                "Skillnad (%)"
        );
        table.addHR();
        Found.DEVELOPMENT_TITLES.forEach(title -> {
            if (developments.has(title) && summary.developments.has(title)) {
                final double val1 = developments.get(title);
                final double val2 = summary.developments.get(title);
                table.addRow(
                        title,
                        format(val1),
                        format(val2),
                        format(val2 - val1)
                );
            }
        });
        table.print();
    }

    private void compareMaps(
            final String title1,
            final String title2,
            final String title3,
            final List<Pair<String, Double>> list1,
            final List<Pair<String, Double>> list2) {
        final Map<String, Comparison> map = new HashMap();
        list1.forEach(p -> {
            addTo(map, p.first(), p.second(), true);
        });
        list2.forEach(p -> {
            addTo(map, p.first(), p.second(), false);
        });
        final List<Comparison> list = new ArrayList(map.values());
        Collections.sort(list, (a, b) -> Double.compare(Math.abs(b.diff), Math.abs((a.diff))));
        final Table table = new Table();
        table.addRow(
                title1,
                String.format("%s (%%)", title2),
                String.format("%s (%%)", title3),
                "Skillnad (%)"
        );
        table.addHR();
        for (int i = 0; i < list.size() && i < 10; ++i) {
            final Comparison comparison = list.get(i);
            table.addRow(
                    comparison.name,
                    format(comparison.val1),
                    format(comparison.val2),
                    format(comparison.diff)
            );
        }
        table.print();
    }

    private void addTo(
            final Map<String, Comparison> map,
            final String name,
            final double value,
            final boolean first) {
        map.putIfAbsent(name, new Comparison(name));
        final Comparison comparison = map.get(name);
        if (first) {
            comparison.val1 += value;
        }
        else {
            comparison.val2 += value;
        }
        comparison.diff = comparison.val2 - comparison.val1;
    }

    private void addToMap(
            final Map<String, Double> map,
            final String key, final double value) {
        if (!map.containsKey(key)) {
            map.put(key, 0.0);
        }
        map.put(key, map.get(key) + value);
    }

    private List<Pair<String, Double>> normalizeMap(final Map<String, Double> map) {
        final List<Pair<String, Double>> res = new ArrayList();
        map.entrySet().forEach(e -> {
            res.add(new Pair(e.getKey(), e.getValue() / percentageSum));
        });
        Collections.sort(res, (a, b) -> Double.compare(b.second(), a.second()));
        return res;
    }

    private String format(final double value) {
        return String.format("%.2f", value);
    }

}

class Comparison {

    public final String name;
    public Double val1, val2, diff;

    public Comparison(final String name) {
        this.name = name;
        val1 = val2 = 0.0;
    }

}
