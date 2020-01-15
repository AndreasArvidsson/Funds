package com.github.andreasarvidsson.founds;

import com.github.andreasarvidsson.founds.util.BaseUtil;
import com.github.andreasarvidsson.founds.util.Pair;
import com.github.andreasarvidsson.founds.util.Table;
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
public class PortfolioSummary extends BaseUtil {

    private final static String SPACE = "   |   ";
    private final String name;
    private final List<FoundData> founds;
    private final List<Pair<String, Double>> countries, sectors, regions;
    private final Developments developments;
    private double percentageSum, avgFee;
    public Double largeCompanies, middleCompanies, smallCompanies,
            sweLargeCompanies, sweMiddleCompanies, sweSmallCompanies;

    public static PortfolioSummary create(
            final String name,
            final List<SelectedFound> selectedFounds) throws IOException {
        return create(name, selectedFounds, false);
    }

    public static PortfolioSummary createInclMorningstar(
            final String name,
            final List<SelectedFound> selectedFounds) throws IOException {
        return create(name, selectedFounds, true);
    }

    public static PortfolioSummary create(
            final String name,
            final List<SelectedFound> selectedFounds,
            final boolean inclMorningstarData) throws IOException {
        final List<FoundData> founds = new ArrayList();
        double sum = 0.0;
        for (final SelectedFound sf : selectedFounds) {
            sum += sf.percentage;
        }
        for (final SelectedFound sd : selectedFounds) {
            founds.add(new FoundData(
                    sd.percentage,
                    sd.percentage / sum,
                    Avanza.getFound(sd.name, sd.alternativeNames),
                    inclMorningstarData ? Morningstar.getFound(sd.name, sd.alternativeNames) : null
            ));
        }
        return new PortfolioSummary(name, founds);
    }

    private PortfolioSummary(
            final String name,
            final List<FoundData> founds) throws IOException {
        this.name = name;
        this.founds = founds;
        developments = new Developments(founds);
        final Map<String, Double> countriesMap = new HashMap();
        final Map<String, Double> sectorsMap = new HashMap();
        final Map<String, Double> regionsMap = new HashMap();
        for (final FoundData fd : founds) {
            percentageSum += fd.percentage * 100;
            avgFee += fd.avanza.productFee * fd.percentageNormalized;
            fd.avanza.countryChartData.forEach(data -> {
                addToMap(countriesMap, data.name, data.y * fd.percentageNormalized);
            });
            fd.avanza.regionChartData.forEach(data -> {
                addToMap(regionsMap, data.name, data.y * fd.percentageNormalized);
            });
            fd.avanza.sectorChartData.forEach(data -> {
                addToMap(sectorsMap, data.name, data.y * fd.percentageNormalized);
            });
            if (fd.morningstar != null) {
                if (largeCompanies == null) {
                    largeCompanies = middleCompanies = smallCompanies = 0.0;
                    sweLargeCompanies = sweMiddleCompanies = sweSmallCompanies = 0.0;
                }
                largeCompanies += fd.morningstar.largeCompanies * fd.percentageNormalized;
                middleCompanies += fd.morningstar.middleCompanies * fd.percentageNormalized;
                smallCompanies += fd.morningstar.smallCompanies * fd.percentageNormalized;
                final double swePercentage = fd.avanza.getRegion(Regions.SWEDEN) / 100;
                sweLargeCompanies += fd.morningstar.largeCompanies * fd.percentageNormalized * swePercentage;
                sweMiddleCompanies += fd.morningstar.middleCompanies * fd.percentageNormalized * swePercentage;
                sweSmallCompanies += fd.morningstar.smallCompanies * fd.percentageNormalized * swePercentage;
            }
        }
        countries = toList(countriesMap);
        regions = toList(regionsMap);
        sectors = toList(sectorsMap);
    }

    public void print() {
        System.out.printf(
                "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                + " %s "
                + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n",
                name
        );
        final Table table = new Table();
        addHeaders(table);
        addFounds(table);
        addSum(table);
        table.print();
        printChartTable();
    }

    private void addHeaders(final Table table) {
        final List<String> headers = new ArrayList();
        headers.addAll(Arrays.asList(
                "Namn", "Andel (%)", "Avgift (%)", "Kategorier",
                "Sverige (%)", "Asien (%)"
        ));
        if (largeCompanies != null) {
            headers.addAll(Arrays.asList(
                    "Stora (%)", "Medelstora (%)", "Små (%)"
            ));
        }
        Developments.DEVELOPMENT_TITLES.forEach(title -> {
            if (developments.showHeader(title)) {
                headers.add(title);
            }
        });
        table.addRow(headers);
        table.addHR();
    }

    private void addFounds(final Table table) {
        for (final FoundData fd : founds) {
            final AvanzaFound found = fd.avanza;
            final List<String> row = new ArrayList();
            row.addAll(Arrays.asList(
                    found.name,
                    format(fd.percentage * 100),
                    format(found.productFee),
                    String.join(", ", found.categories),
                    format(fd.avanza.getRegion(Regions.SWEDEN)),
                    format(fd.avanza.getRegion(Regions.ASIA))
            ));
            if (largeCompanies != null) {
                if (fd.morningstar != null) {
                    row.addAll(Arrays.asList(
                            format(fd.morningstar.largeCompanies),
                            format(fd.morningstar.middleCompanies),
                            format(fd.morningstar.smallCompanies)
                    ));
                }
                else {
                    row.addAll(Arrays.asList("", "", ""));
                }
            }
            Developments.getDevelopment(found).forEach(d -> {
                row.add(format(d.second()));
            });
            table.addRow(row);
        }

        table.addHR();
    }

    private void addSum(final Table table) {
        final List<String> row = new ArrayList();
        row.addAll(Arrays.asList(
                "",
                format(percentageSum),
                format(avgFee),
                "",
                getRegion(Regions.SWEDEN),
                getRegion(Regions.ASIA)
        ));
        if (largeCompanies != null) {
            row.addAll(Arrays.asList(
                    format(largeCompanies),
                    format(middleCompanies),
                    format(smallCompanies)
            ));
        }
        Developments.DEVELOPMENT_TITLES.forEach(title -> {
            if (developments.has(title)) {
                row.add(format(developments.get(title)));
            }
        });
        table.addRow(row);
    }

    private void printChartTable() {
        final Table table = new Table();
        final List<String> row = new ArrayList();
        row.addAll(Arrays.asList(
                "Land", "Andel (%)", SPACE,
                "Region", "Andel (%)", SPACE,
                "Bransch", "Andel (%)"
        ));
        List<Pair<String, Double>> sizeList = new ArrayList();
        if (largeCompanies != null) {
            row.addAll(Arrays.asList(
                    SPACE, "Storlek", "Andel (%)"
            ));
            sizeList = Arrays.asList(
                    new Pair("Stora bolag", largeCompanies),
                    new Pair("Medelstora bolag", middleCompanies),
                    new Pair("Små bolag", smallCompanies),
                    new Pair("Stora svenska bolag", sweLargeCompanies),
                    new Pair("Medelstora svenska bolag", sweMiddleCompanies),
                    new Pair("Små svenska bolag", sweSmallCompanies)
            );
        }
        table.addRow(row);
        table.addHR();
        final int size = Math.min(
                10,
                max(countries.size(), regions.size(), sectors.size(), sizeList.size())
        );
        for (int i = 0; i < size; ++i) {
            row.clear();
            row.addAll(Arrays.asList(
                    i < countries.size() ? countries.get(i).first() : "",
                    i < countries.size() ? format(countries.get(i).second()) : "",
                    SPACE,
                    i < regions.size() ? regions.get(i).first() : "",
                    i < regions.size() ? format(regions.get(i).second()) : "",
                    SPACE,
                    i < sectors.size() ? sectors.get(i).first() : "",
                    i < sectors.size() ? format(sectors.get(i).second()) : ""
            ));
            if (!sizeList.isEmpty()) {
                row.addAll(Arrays.asList(
                        SPACE,
                        i < sizeList.size() ? sizeList.get(i).first() : "",
                        i < sizeList.size() ? format(sizeList.get(i).second()) : ""
                ));
            }
            table.addRow(row);
        }
        table.print();
    }

    private String getRegion(
            final String regionName) {
        for (final Pair<String, Double> pair : regions) {
            if (regionName.equals(pair.first())) {
                return format(pair.second());
            }
        }
        return "-";
    }

    public void compare(final PortfolioSummary summary) {
        System.out.printf(
                "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                + " %s vs %s "
                + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n",
                name, summary.name
        );
        final Table table = new Table();
        addBasicCompareTable(summary, table);
        addChartCompareTables(summary, table);
        table.print();
    }

    private void addBasicCompareTable(final PortfolioSummary summary, final Table table) {
        final List<String> headers = new ArrayList(Arrays.asList(
                "", name, summary.name, "Skillnad", SPACE
        ));
        final List<List<String>> rows = new ArrayList();
        rows.add(new ArrayList(Arrays.asList(
                "# Fonder",
                Integer.toString(founds.size()),
                Integer.toString(summary.founds.size()),
                Integer.toString(summary.founds.size() - founds.size()),
                SPACE
        )));
        addRow(
                rows, true, 1, "Andel (%)", percentageSum, summary.percentageSum
        );
        addRow(
                rows, true, 2, "Avgift (%)", avgFee, summary.avgFee
        );
        if (largeCompanies != null && summary.largeCompanies != null) {
            headers.addAll(Arrays.asList(
                    "Storlek",
                    String.format("%s (%%)", name),
                    String.format("%s (%%)", summary.name),
                    "Skillnad (%)"
            ));
            int i = 0;
            addRow(
                    rows, false, i++, "Stora bolag", largeCompanies, summary.largeCompanies
            );
            addRow(
                    rows, false, i++, "Medelstora bolag", middleCompanies, summary.middleCompanies
            );
            addRow(
                    rows, false, i++, "Små bolag", smallCompanies, summary.smallCompanies
            );
            addRow(
                    rows, false, i++, "Stora svenska bolag", sweLargeCompanies, summary.sweLargeCompanies
            );
            addRow(
                    rows, false, i++, "Medelstora svenska bolag", sweMiddleCompanies, summary.sweMiddleCompanies
            );
            addRow(
                    rows, false, i++, "Små svenska bolag", sweSmallCompanies, summary.sweSmallCompanies
            );
        }
        table.addRow(headers);
        table.addHR();
        rows.forEach(row -> {
            table.addRow(row);
        });
        table.addRow("");
    }

    private void addChartCompareTables(final PortfolioSummary summary, final Table table) {
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

    private List<Pair<String, Double>> toList(final Map<String, Double> map) {
        final List<Pair<String, Double>> res = new ArrayList();
        map.entrySet().forEach(e -> {
            res.add(new Pair(e.getKey(), e.getValue()));
        });
        Collections.sort(res, (a, b) -> Double.compare(b.second(), a.second()));
        return res;
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
