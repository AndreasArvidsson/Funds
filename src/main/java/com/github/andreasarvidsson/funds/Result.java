package com.github.andreasarvidsson.funds;

import com.github.andreasarvidsson.funds.Country.Region;
import com.github.andreasarvidsson.funds.util.AsciiTable;
import com.github.andreasarvidsson.funds.util.Comparison;
import com.github.andreasarvidsson.funds.util.Excel;
import com.github.andreasarvidsson.funds.util.Excel.ExcelTable;
import com.github.andreasarvidsson.funds.util.Values;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public class Result {

    private final static String SPACE = "   |   ";
    private final static String MISSING = "-";
    private final Excel excel = new Excel();
    private final StringBuilder sb = new StringBuilder("\n");
    private final long t0 = System.currentTimeMillis();

    public void add(final Portfolio portfolio) {
        addFundsTable(portfolio);
        addStatsTable(portfolio);
    }

    public void add(final Rankings rankings) {
        final AsciiTable asciiTable = new AsciiTable();
        final ExcelTable excelTable = excel.getTable("Rankning");

        final String title = String.format("Rankning, %s", rankings.name);
        sb.append(getAsciiTitle(title));
        excelTable.addTitle(getExcelTitle(title));
        excelTable.addRow();

        final List<String> headersRow = getRankingsHeadersRow(rankings);
        asciiTable.addRow(headersRow);
        asciiTable.addHR();
        excelTable.addRow(headersRow);
        excelTable.addHR();

        final List<List<String>> rows = getRankingsRows(rankings);
        asciiTable.addRows(rows);
        excelTable.addRows(rows);

        asciiTable.addRow();
        excelTable.addRow();
        sb.append(asciiTable.toString());
        excelTable.autoSizeColumns(headersRow.size());
    }

    public void compare(final Portfolio p1, final Portfolio p2) {
        final AsciiTable asciiTable = new AsciiTable();
        final ExcelTable excelTable = excel.getTable("Jämförelse");

        final String title = String.format("%s vs %s", p1.name, p2.name);
        sb.append(getAsciiTitle(title));
        excelTable.addTitle(getExcelTitle(title));
        excelTable.addRow();

        final List<String> headersRow1 = getCompareHeadersRow(p1, p2);
        asciiTable.addRow(headersRow1);
        asciiTable.addHR();
        excelTable.addRow(headersRow1);
        excelTable.addHR();

        final List<List<String>> rows1 = getCompareRows(p1, p2);
        asciiTable.addRows(rows1);
        excelTable.addRows(rows1);
        asciiTable.addRow();
        excelTable.addRow();

        final List<String> headersRow2 = getCompareHeadersRow(p1, p2, Headers.HOLDINGS, Headers.SECTOR);
        asciiTable.addRow(headersRow2);
        asciiTable.addHR();
        excelTable.addRow(headersRow2);
        excelTable.addHR();

        final List<List<String>> rows2 = new ArrayList<>();
        compareValues(rows2, true, p1.holdings, p2.holdings);
        compareValues(rows2, false, p1.sectors, p2.sectors);
        asciiTable.addRows(rows2);
        excelTable.addRows(rows2);
        asciiTable.addRow();
        excelTable.addRow();

        final List<String> headersRow3 = getCompareHeadersRow(p1, p2, Headers.LAND, Headers.REGION);
        asciiTable.addRow(headersRow3);
        asciiTable.addHR();
        excelTable.addRow(headersRow3);
        excelTable.addHR();

        final List<List<String>> rows3 = new ArrayList<>();
        compareValues(rows3, true, p1.countries, p2.countries);
        compareValues(rows3, false, p1.regions, p2.regions);
        asciiTable.addRows(rows3);
        excelTable.addRows(rows3);
        asciiTable.addRow();
        excelTable.addRow();

        //TODO missing size info for now
        //        if (!p1.companiesSize.isEmpty() && !p2.companiesSize.isEmpty()) {
//            res.addAll(Arrays.asList(
//                    "Storlek",
//                    String.format("%s (%%)", p1.name),
//                    String.format("%s (%%)", p2.name),
//                    Headers.DIFFERENCE
//            ));
//        }
//        else {
//            res.addAll(Arrays.asList("", "", "", ""));
//        }
//  if (!p1.companiesSize.isEmpty() && !p2.companiesSize.isEmpty()) {
//            compareValues(res, false, p1.companiesSize, p2.companiesSize);
//        }
        sb.append(asciiTable.toString());
        excelTable.autoSizeColumns(headersRow1.size());
    }

    public void print() {
        System.out.println(toString());
        System.out.printf("Elapsed time: %.1fs\n\n", (System.currentTimeMillis() - t0) * 0.001);
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    public void save() throws FileNotFoundException, IOException {
        if (!excel.hasTable("Om")) {
            final ExcelTable excelTable = excel.getTable("Om");
            excelTable.addRow(Arrays.asList("Skapad av", "Andreas Arvidsson"));
            excelTable.addRow(Arrays.asList("Källa", "https://github.com/AndreasArvidsson/Funds"));
            excelTable.autoSizeColumns(2);
        }

        excel.save(new File(String.format(
                "excel/Fonderportföljer, %s.xls", getTimeStamp()
        )));
    }

    private void addFundsTable(final Portfolio portfolio) {
        final AsciiTable asciiTable = new AsciiTable();
        final ExcelTable excelTable = excel.getTable("Fonder");

        sb.append(getAsciiTitle(portfolio.name));
        excelTable.addTitle(getExcelTitle(portfolio.name));
        excelTable.addRow();

        final List<String> headersRow = getFundsHeadersRow(portfolio);
        asciiTable.addRow(headersRow);
        asciiTable.addHR();
        excelTable.addRow(headersRow);
        excelTable.addHR();

        final List<List<String>> fundRows = getFundRows(portfolio);
        asciiTable.addRows(fundRows);
        excelTable.addRows(fundRows);

        final List<String> sumRow = getSumRow(portfolio);
        asciiTable.addHR();
        asciiTable.addRow(sumRow);
        excelTable.addHR();
        excelTable.addRow(sumRow);

        asciiTable.addRow();
        excelTable.addRow();
        sb.append(asciiTable.toString());
        excelTable.autoSizeColumns(headersRow.size());
    }

    private void addStatsTable(final Portfolio portfolio) {
        final AsciiTable asciiTable = new AsciiTable();
        final ExcelTable excelTable = excel.getTable("Statistik");

        excelTable.addTitle(getExcelTitle(portfolio.name));
        excelTable.addRow();

        final List<String> headersRow = getStatsHeaderRow(portfolio);
        asciiTable.addRow(headersRow);
        asciiTable.addHR();
        excelTable.addRow(headersRow);
        excelTable.addHR();

        final List<List<String>> statRows = getStatsRows(portfolio);
        asciiTable.addRows(statRows);
        excelTable.addRows(statRows);

        asciiTable.addRow();
        excelTable.addRow();
        sb.append(asciiTable.toString());
        excelTable.autoSizeColumns(headersRow.size());
    }

    private String getAsciiTitle(final String name) {
        return String.format("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                + " %s "
                + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n",
                name);
    }

    private String getExcelTitle(final String name) {
        return String.format("> %s", name);
    }

    private List<String> getFundsHeadersRow(final Portfolio p) {
        final List<String> res = new ArrayList<>();
        res.addAll(Arrays.asList("Namn", Headers.PERCENTAGE, Headers.FEE, Headers.RISK,
                Headers.STANDARD_DEVIATION, Headers.SHARPE_RATIO,
                "Kategorier", "Sverige (%)", "USA (%)", "Asien (%)", Headers.NON_DEVELOPED_MARKETS
        ));
        if (!p.companiesSize.isEmpty()) {
            res.addAll(Arrays.asList(
                    "Stora (%)", "Medelstora (%)", "Små (%)"
            ));
        }
        res.addAll(Headers.DEVELOPMENT_TITLES);
        return res;
    }

    private List<List<String>> getFundRows(final Portfolio p) {
        final List<List<String>> res = new ArrayList<>();
        p.funds.forEach(fd -> {
            final List<String> row = new ArrayList<>();
            final AvanzaFund fund = fd.avanza;
            row.addAll(Arrays.asList(
                    fund.name,
                    format(fd.percentage * 100),
                    format(fd.getFee()),
                    Integer.toString(fund.risk),
                    fund.standardDeviation != null ? format(fund.standardDeviation) : MISSING,
                    fund.sharpeRatio != null ? format(fund.sharpeRatio) : MISSING,
                    String.join(", ", fund.categories),
                    format(fd.avanza.getCountry(Country.SWEDEN, 0.0)),
                    format(fd.avanza.getCountry(Country.USA, 0.0)),
                    format(fd.avanza.getRegion(Region.ASIA, 0.0)),
                    format(fd.avanza.getNonDevelopedMarkets())
            ));
            if (!p.companiesSize.isEmpty()) {
                //TODO missing size info for now
//                if (fd.morningstar != null) {
//                    row.addAll(Arrays.asList(
//                            format(fd.morningstar.largeCompanies),
//                            format(fd.morningstar.middleCompanies),
//                            format(fd.morningstar.smallCompanies)
//                    ));
//                }
//                else {
//                row.addAll(Arrays.asList("", "", ""));
//                }
            }
            Headers.DEVELOPMENT_TITLES.forEach(key -> {
                row.add(format(fund.getDevelopment(key, null)));
            });
            res.add(row);
        });
        return res;
    }

    private List<String> getSumRow(final Portfolio p) {
        final List<String> res = new ArrayList<>();
        res.addAll(Arrays.asList(
                "",
                format(p.percentageSum),
                format(p.avgFee),
                format(p.risk),
                format(p.sum.get(Headers.STANDARD_DEVIATION, null)),
                format(p.sum.get(Headers.SHARPE_RATIO, null)),
                "",
                format(p.countries.get(Country.SWEDEN.name, 0.0)),
                format(p.countries.get(Country.USA.name, 0.0)),
                format(p.regions.get(Region.ASIA.name, 0.0)),
                format(p.nonDevelopedMarkets)
        ));
        if (!p.companiesSize.isEmpty()) {
            res.addAll(Arrays.asList(
                    format(p.companiesSize.get(0).second()),
                    format(p.companiesSize.get(1).second()),
                    format(p.companiesSize.get(2).second())
            ));
        }
        Headers.DEVELOPMENT_TITLES.forEach(title -> {
            if (p.developments.has(title)) {
                res.add(format(p.developments.get(title, null)));
            }
        });
        return res;
    }

    private List<String> getStatsHeaderRow(final Portfolio p) {
        final List<String> res = new ArrayList<>();
        res.addAll(Arrays.asList(
                Headers.HOLDINGS, Headers.PERCENTAGE, SPACE,
                Headers.SECTOR, Headers.PERCENTAGE, SPACE,
                Headers.LAND, Headers.PERCENTAGE, SPACE,
                Headers.REGION, Headers.PERCENTAGE
        ));
        if (!p.companiesSize.isEmpty()) {
            res.addAll(Arrays.asList(
                    SPACE, "Storlek", Headers.PERCENTAGE
            ));
        }
        return res;
    }

    private List<List<String>> getStatsRows(final Portfolio p) {
        final List<List<String>> res = new ArrayList<>();
        final int size = Math.min(
                10,
                max(
                        p.countries.size(), p.regions.size(),
                        p.sectors.size(), p.companiesSize.size() + 1
                )
        );
        for (int i = 0; i < size; ++i) {
            final List<String> row = new ArrayList<>();
            row.addAll(Arrays.asList(
                    i < p.holdings.size() ? p.holdings.get(i).first() : "",
                    i < p.holdings.size() ? format(p.holdings.get(i).second()) : "",
                    SPACE,
                    i < p.sectors.size() ? p.sectors.get(i).first() : "",
                    i < p.sectors.size() ? format(p.sectors.get(i).second()) : "",
                    SPACE,
                    i < p.countries.size() ? p.countries.get(i).first() : "",
                    i < p.countries.size() ? format(p.countries.get(i).second()) : "",
                    SPACE,
                    i < p.regions.size() ? p.regions.get(i).first() : "",
                    i < p.regions.size() ? format(p.regions.get(i).second()) : ""
            ));
            if (!p.companiesSize.isEmpty()) {
                //Add blank row between global and swedish companies.
                if (i == 3) {
                    row.add(SPACE);
                }
                else {
                    final int j = i < 3 ? i : i - 1;
                    row.addAll(Arrays.asList(
                            SPACE,
                            j < p.companiesSize.size() ? p.companiesSize.get(j).first() : "",
                            j < p.companiesSize.size() ? format(p.companiesSize.get(j).second()) : ""
                    ));
                }
            }
            res.add(row);
        }
        return res;
    }

    private List<String> getCompareHeadersRow(final Portfolio p1, final Portfolio p2) {
        final List<String> res = new ArrayList<>(Arrays.asList(
                "", p1.name, p2.name, "Skillnad",
                SPACE,
                "Utveckling",
                String.format("%s (%%)", p1.name),
                String.format("%s (%%)", p2.name),
                Headers.DIFFERENCE
        ));
        return res;
    }

    private List<String> getCompareHeadersRow(
            final Portfolio p1, final Portfolio p2,
            final String title1, final String title2) {
        return Arrays.asList(
                title1,
                String.format("%s (%%)", p1.name),
                String.format("%s (%%)", p2.name),
                Headers.DIFFERENCE,
                SPACE,
                title2,
                String.format("%s (%%)", p1.name),
                String.format("%s (%%)", p2.name),
                Headers.DIFFERENCE
        );
    }

    private List<List<String>> getCompareRows(final Portfolio p1, final Portfolio p2) {
        final List<List<String>> res = new ArrayList<>();
        res.add(new ArrayList<>(Arrays.asList(
                "# Fonder",
                Integer.toString(p1.funds.size()),
                Integer.toString(p2.funds.size()),
                Integer.toString(p2.funds.size() - p1.funds.size()),
                SPACE
        )));
        addRow(
                res, true, 1, Headers.PERCENTAGE, p1.percentageSum, p2.percentageSum
        );
        addRow(
                res, true, 2, Headers.FEE, p1.avgFee, p2.avgFee
        );
        addRow(
                res, true, 3, Headers.RISK, p1.risk, p2.risk
        );
        addRow(res, true, 4, Headers.STANDARD_DEVIATION, p1.sum.get(Headers.STANDARD_DEVIATION, null), p2.sum.get(Headers.STANDARD_DEVIATION, null));
        addRow(res, true, 5, Headers.SHARPE_RATIO, p1.sum.get(Headers.SHARPE_RATIO, null), p2.sum.get(Headers.SHARPE_RATIO, null));
        addRow(res, true, 6, Headers.NON_DEVELOPED_MARKETS, p1.nonDevelopedMarkets, p2.nonDevelopedMarkets);
        compareDevelopments(res, false, p1, p2);
        return res;
    }

    private List<List<String>> getRankingsRows(final Rankings rankings) {
        final List<List<String>> res = new ArrayList<>();
        rankings.funds.forEach(fr -> {
            final List<String> row = new ArrayList<>();
            row.add(fr.avanza.name);
            fr.values.forEach(p -> {
                row.add(String.format("%d (%s)", p.first(), format(p.second())));
            });
            row.add(format(fr.mean));
            res.add(row);
        });
        return res;
    }

    private List<String> getRankingsHeadersRow(final Rankings rankings) {
        final List<String> res = new ArrayList<>();
        res.add("");
        res.addAll(rankings.headers);
        res.add("Medel");
        return res;
    }

    private void compareDevelopments(
            final List<List<String>> rows,
            final boolean first,
            final Portfolio p1,
            final Portfolio p2) {
        for (int i = 0; i < Headers.DEVELOPMENT_TITLES.size(); ++i) {
            final String title = Headers.DEVELOPMENT_TITLES.get(i);
            addRow(rows, first, i, title,
                    p1.developments.get(title, null),
                    p2.developments.get(title, null)
            );
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
            final Double val1,
            final Double val2) {
        if (i >= rows.size()) {
            rows.add(new ArrayList<>());
            if (!first) {
                rows.get(i).addAll(Arrays.asList("", "", "", "", SPACE));
            }
        }
        rows.get(i).addAll(Arrays.asList(
                title,
                format(val1),
                format(val2),
                val1 != null && val2 != null ? format(val2 - val1) : MISSING
        ));
        if (first) {
            rows.get(i).add(SPACE);
        }
    }

    private String format(final Double value) {
        if (value == null) {
            return MISSING;
        }
        return String.format("%.2f", Math.round(value * 100.0) / 100.0);
    }

    private int max(final int value, final int... values) {
        int res = value;
        for (int i = 0; i < values.length; ++i) {
            if (values[i] > res) {
                res = values[i];
            }
        }
        return res;
    }

    private String getTimeStamp() {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm");
        final Date date = new Date();
        return format.format(date);
    }

}
