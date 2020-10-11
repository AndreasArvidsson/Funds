package com.github.andreasarvidsson.funds;

import com.github.andreasarvidsson.funds.util.Pair;
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
    public final List<FundRank> funds = new ArrayList();
    public final List<String> headers = new ArrayList();

    public Rankings(final String name, final String... fundNames) throws IOException {
        this(name, false, fundNames);
    }

    public Rankings(final String name, final boolean useSavr, final String... fundNames) throws IOException {
        this.name = name;
        for (final String fundName : fundNames) {
            final FundRank fr = new FundRank(
                    Avanza.getFund(fundName),
                    useSavr ? SAVR.getFund(fundName) : null
            );
            funds.add(fr);
        }
        final List<Pair<FundRank, Double>> fees = getFees();
        addValues("Avgift", fees);
        final List<Pair<FundRank, Double>> sharpeRatio = getSharpeRatio();
        if (sharpeRatio != null) {
            addValues(Headers.SHARPE_RATIO, sharpeRatio);
        }
        for (int i = 0; i < 4; ++i) {
            final List<Pair<FundRank, Double>> values = getValues(i);
            if (values == null) {
                break;
            }
            addValues(DEV_HEADERS.get(i), values);
        }
        Collections.sort(funds, (a, b) -> Integer.compare(b.points, a.points));
    }

    private void addValues(final String header, final List<Pair<FundRank, Double>> values) {
        headers.add(header);
        for (int j = 0; j < values.size(); ++j) {
            final Pair<FundRank, Double> p = values.get(j);
            final FundRank fr = p.first();
            final int points = j + 1;
            fr.points += points;
            fr.values.add(new Pair(points, p.second()));
        }
    }

    private List<Pair<FundRank, Double>> getFees() {
        final List<Pair<FundRank, Double>> res = new ArrayList();
        funds.forEach(fund -> {
            res.add(new Pair(fund, fund.getFee()));
        });
        Collections.sort(res, (a, b) -> Double.compare(b.second(), a.second()));
        return res;
    }

    private List<Pair<FundRank, Double>> getSharpeRatio() {
        final List<Pair<FundRank, Double>> res = new ArrayList();
        for (final FundRank fund : funds) {
            if (fund.avanza.sharpeRatio == null) {
                return null;
            }
            res.add(new Pair(fund, fund.avanza.sharpeRatio));
        }
        Collections.sort(res, (a, b) -> Double.compare(a.second(), b.second()));
        return res;
    }

    private List<Pair<FundRank, Double>> getValues(final int level) {
        final List<Pair<FundRank, Double>> res = new ArrayList();
        for (final FundRank fund : funds) {
            final Double value = getValue(fund.avanza, level);
            if (value == null) {
                return null;
            }
            res.add(new Pair(fund, value));
        }
        Collections.sort(res, (a, b) -> Double.compare(a.second(), b.second()));
        return res;
    }

    private Double getValue(final AvanzaFund fund, final int level) {
        switch (level) {
            case 0:
                return fund.developmentSixMonths;
            case 1:
                return fund.developmentOneYear;
            case 2:
                return fund.developmentThreeYears;
            case 3:
                return fund.developmentFiveYears;
            default:
                return null;
        }
    }

    public static class FundRank {

        public final AvanzaFund avanza;
        public final SavrFund savr;
        public int points = 0;
        public final List<Pair<Integer, Double>> values = new ArrayList();

        public FundRank(final AvanzaFund avanza, final SavrFund savr) {
            this.avanza = avanza;
            this.savr = savr;
        }

        public double getFee() {
            return savr != null ? savr.fee : avanza.productFee;
        }

    }

}
