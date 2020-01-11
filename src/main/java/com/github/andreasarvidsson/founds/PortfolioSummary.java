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
public class PortfolioSummary {

    public final String name;
    public double percentageSum, avgFee;
    public final List<Pair<Found, Double>> founds;
    public List<Pair<String, Double>> countries, sectors, regions;
    public final Developments developments;
    private final static String SPACE = "   |   ";

    public static PortfolioSummary create(
            final String name,
            final List<Pair<String, Double>> foundNames) throws IOException {
        return new PortfolioSummary(
                name,
                Avanza.getFoundPairs(foundNames)
        );
    }

    public PortfolioSummary(
            final String name,
            final List<Pair<Found, Double>> founds) throws IOException {
        this.name = name;
        this.founds = founds;
        developments = new Developments(founds);
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
            });
            found.regionChartData.forEach(data -> {
                addToMap(regionsMap, data.name, data.y * percentage);
            });
            found.sectorChartData.forEach(data -> {
                addToMap(sectorsMap, data.name, data.y * percentage);
            });
        }
        avgFee /= percentageSum;
        countries = normalizeMap(countriesMap);
        regions = normalizeMap(regionsMap);
        sectors = normalizeMap(sectorsMap);
    }

    public void print() {
        System.out.printf(
                "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                + " %s "
                + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n",
                name
        );
        final Table table = new Table();

        table.addHeaders(
                "Namn", "Andel (%)", "Avgift (%)", "Kategorier",
                "Sverige (%W)", "Asien (%W)"
        );
        table.addHeaders(Developments.DEVELOPMENT_TITLES);

        for (final Pair<Found, Double> foundPair : founds) {
            final Found found = foundPair.first();
            final double percentage = foundPair.second();
            final List<String> row = new ArrayList();
            row.addAll(Arrays.asList(
                    found.name,
                    format(percentage * 100),
                    format(found.productFee),
                    String.join(", ", found.categories),
                    getRegion(found, percentage, Regions.SWEDEN),
                    getRegion(found, percentage, Regions.ASIA)
            ));
            Developments.getDevelopment(found).forEach(d -> {
                row.add(format(d.second()));
            });
            table.addRow(row);
        }

        table.addHR();

        final List<String> row = new ArrayList();
        row.addAll(Arrays.asList(
                "",
                format(percentageSum * 100),
                format(avgFee),
                "",
                getRegion(Regions.SWEDEN),
                getRegion(Regions.ASIA)
        ));
        Developments.DEVELOPMENT_TITLES.forEach(title -> {
            if (developments.has(title)) {
                row.add(format(developments.get(title)));
            }
        });
        table.addRow(row);

        table.print();

        table.reset();
        table.addRow(
                "Land", "Andel (%)", SPACE,
                "Region", "Andel (%)", SPACE,
                "Bransch", "Andel (%)"
        );
        table.addHR();
        for (int i = 0; i < 10; ++i) {
            table.addRow(Arrays.asList(
                    i < countries.size() ? countries.get(i).first() : "",
                    i < countries.size() ? format(countries.get(i).second()) : "",
                    SPACE,
                    i < regions.size() ? regions.get(i).first() : "",
                    i < regions.size() ? format(regions.get(i).second()) : "",
                    SPACE,
                    i < sectors.size() ? sectors.get(i).first() : "",
                    i < sectors.size() ? format(sectors.get(i).second()) : ""
            ));
        }
        table.print();
    }

    private String getRegion(
            final Found found,
            final double percentage,
            final String regionName) {
        final double value = found.getRegion(regionName);
        return String.format("%.2f", value * percentage / percentageSum);
    }

    private String getRegion(
            final String regionName) {
        for (final Pair<String, Double> pair : regions) {
            if (regionName.equals(pair.first())) {
                return String.format("%.2f", pair.second());
            }
        }
        return "0.0";
    }

    public void compare(final PortfolioSummary summary) {
        System.out.printf(
                "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                + " %s vs %s "
                + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n",
                name, summary.name
        );
        final Table table = new Table();

        table.addHeaders("", name, summary.name, "Skillnad");
        table.addRow(
                "# Fonder",
                Integer.toString(founds.size()),
                Integer.toString(summary.founds.size()),
                Integer.toString(summary.founds.size() - founds.size())
        );
        table.addRow(
                "Andel (%)",
                format(percentageSum * 100),
                format(summary.percentageSum * 100),
                format((summary.percentageSum - percentageSum) * 100)
        );
        table.addRow(
                "Avgift (%)",
                format(avgFee),
                format(summary.avgFee),
                format(summary.avgFee - avgFee)
        );
        table.addRow("");

        final List<List<String>> rows = new ArrayList();
        compareMaps(rows, true, countries, summary.countries);
        compareMaps(rows, false, regions, summary.regions);
        addHeaderRow(table, summary, "Land", "Region");
        rows.forEach(row -> {
            table.addRow(row);
        });
        table.addRow("");

        rows.clear();
        compareMaps(rows, true, sectors, summary.sectors);
        compareDevelopments(rows, false, summary);
        addHeaderRow(table, summary, "Bransch", "Utveckling");
        rows.forEach(row -> {
            table.addRow(row);
        });
        table.print();
    }

    private void addHeaderRow(
            final Table table,
            final PortfolioSummary summary,
            final String title1, final String title2) {
        table.addRow(
                title1,
                String.format("%s (%%)", name),
                String.format("%s (%%)", summary.name),
                "Skillnad (%)",
                SPACE,
                title2,
                String.format("%s (%%)", name),
                String.format("%s (%%)", summary.name),
                "Skillnad (%)"
        );
        table.addHR();
    }

    private void compareDevelopments(
            final List<List<String>> rows,
            final boolean first,
            final PortfolioSummary summary) {
        for (int i = 0; i < Developments.DEVELOPMENT_TITLES.size(); ++i) {
            final String title = Developments.DEVELOPMENT_TITLES.get(i);
            if (developments.has(title) && summary.developments.has(title)) {
                addRow(
                        rows, first, i, title,
                        developments.get(title),
                        summary.developments.get(title)
                );
            }
        }
    }

    private void compareMaps(
            final List<List<String>> rows,
            final boolean first,
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
        Collections.sort(list, (a, b)
                -> Double.compare(Math.abs(b.diff), Math.abs((a.diff)))
        );

        for (int i = 0; i < list.size() && i < 10; ++i) {
            final Comparison comparison = list.get(i);
            addRow(
                    rows, first, i, comparison.name, comparison.val1, comparison.val2
            );
        }
    }

    final void addRow(
            final List<List<String>> rows,
            final boolean first,
            final int i,
            final String title,
            final double val1,
            final double val2) {
        if (i >= rows.size()) {
            rows.add(new ArrayList());
            if (!first) {
                rows.get(i).addAll(Arrays.asList("", "", "", "", SPACE));
            }
        }
        rows.get(i).addAll(Arrays.asList(
                title,
                format(val1),
                format(val2),
                format(val2 - val1)
        ));
        if (first) {
            rows.get(i).add(SPACE);
        }
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
