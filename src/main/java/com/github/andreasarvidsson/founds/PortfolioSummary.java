package com.github.andreasarvidsson.founds;

import com.github.andreasarvidsson.founds.util.BaseUtil;
import com.github.andreasarvidsson.founds.util.Comparison;
import com.github.andreasarvidsson.founds.util.Table;
import com.github.andreasarvidsson.founds.util.Values;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public class PortfolioSummary extends BaseUtil {

    private final static String SPACE = "   |   ";
    private final String name;
    private final List<FoundData> founds;
    private final Developments developments;
    private double percentageSum, avgFee;
    final Values companiesSize = new Values();
    final Values countries = new Values();
    final Values sectors = new Values();
    final Values regions = new Values();

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
        for (final FoundData fd : founds) {
            percentageSum += fd.percentage * 100;
            avgFee += fd.avanza.productFee * fd.percentageNormalized;
            fd.avanza.countryChartData.forEach(data -> {
                countries.add(data.name, data.y * fd.percentageNormalized);
            });
            fd.avanza.regionChartData.forEach(data -> {
                regions.add(data.name, data.y * fd.percentageNormalized);
            });
            fd.avanza.sectorChartData.forEach(data -> {
                sectors.add(data.name, data.y * fd.percentageNormalized);
            });
            if (fd.morningstar != null) {
                companiesSize.add("Stora bolag", fd.morningstar.largeCompanies * fd.percentageNormalized);
                companiesSize.add("Medelstora bolag", fd.morningstar.middleCompanies * fd.percentageNormalized);
                companiesSize.add("Små bolag", fd.morningstar.smallCompanies * fd.percentageNormalized);
                final double swePercentage = fd.avanza.getRegion(Regions.SWEDEN) / 100;
                companiesSize.add("Stora svenska bolag", fd.morningstar.largeCompanies * fd.percentageNormalized * swePercentage);
                companiesSize.add("Medelstora svenska bolag", fd.morningstar.middleCompanies * fd.percentageNormalized * swePercentage);
                companiesSize.add("Små svenska bolag", fd.morningstar.smallCompanies * fd.percentageNormalized * swePercentage);
            }
        }
        countries.compile(true);
        regions.compile(true);
        sectors.compile(true);
        companiesSize.compile();
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
        if (!companiesSize.isEmpty()) {
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
            if (!companiesSize.isEmpty()) {
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
                format(regions.get(Regions.SWEDEN)),
                format(regions.get(Regions.ASIA))
        ));
        if (!companiesSize.isEmpty()) {
            row.addAll(Arrays.asList(
                    format(companiesSize.get(0).second()),
                    format(companiesSize.get(1).second()),
                    format(companiesSize.get(2).second())
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
        if (!companiesSize.isEmpty()) {
            row.addAll(Arrays.asList(
                    SPACE, "Storlek", "Andel (%)"
            ));
        }
        table.addRow(row);
        table.addHR();
        final int size = Math.min(
                10,
                max(countries.size(), regions.size(), sectors.size(), companiesSize.size())
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
            if (!companiesSize.isEmpty()) {
                row.addAll(Arrays.asList(
                        SPACE,
                        i < companiesSize.size() ? companiesSize.get(i).first() : "",
                        i < companiesSize.size() ? format(companiesSize.get(i).second()) : ""
                ));
            }
            table.addRow(row);
        }
        table.print();
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
        if (!companiesSize.isEmpty() && !summary.companiesSize.isEmpty()) {
            headers.addAll(Arrays.asList(
                    "Storlek",
                    String.format("%s (%%)", name),
                    String.format("%s (%%)", summary.name),
                    "Skillnad (%)"
            ));
            compareValues(rows, false, companiesSize, summary.companiesSize);
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
        compareValues(rows, true, countries, summary.countries);
        compareValues(rows, false, regions, summary.regions);
        addHeaderRow(table, summary, "Land", "Region");
        rows.forEach(row -> {
            table.addRow(row);
        });
        table.addRow("");

        rows.clear();
        compareValues(rows, true, sectors, summary.sectors);
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

    private void compareValues(
            final List<List<String>> rows,
            final boolean first,
            final Values values1,
            final Values values2) {
        final Comparison comparison = new Comparison();
        values1.forEach(p -> {
            comparison.putFirst(p.first(), p.second());
        });
        values2.forEach(p -> {
            comparison.putSecond(p.first(), p.second());
        });
        comparison.compile();
        for (int i = 0; i < comparison.size() && i < 10; ++i) {
            final String key = comparison.get(i);
            addRow(
                    rows, first, i, key, comparison.first(key), comparison.second(key)
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

}
