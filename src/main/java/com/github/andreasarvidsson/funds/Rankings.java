package com.github.andreasarvidsson.funds;

import com.github.andreasarvidsson.funds.util.Mean;
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
    public final List<FundRank> funds = new ArrayList<>();
    public final List<String> headers = new ArrayList<>();

    public Rankings(final String name, final SelectedFund... selectedFunds) throws IOException {
        this(name, Arrays.asList(selectedFunds));
    }

    public Rankings(final String name, final List<SelectedFund> selectedFunds) throws IOException {
        this.name = name;
        for (final SelectedFund sf : selectedFunds) {
            final FundRank fr = new FundRank(
                    Avanza.getFund(sf.name),
                    sf.fee
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
        calculatePoints();
        Collections.sort(funds, (a, b) -> Double.compare(a.mean, b.mean));
    }

    private void calculatePoints() {
        funds.forEach(fr -> {
            final Mean mean = new Mean();
            for (final Pair<Integer, Double> p : fr.values) {
                mean.add(p.first());
            }
            fr.mean = mean.geometric();
        });
    }

    private void addValues(final String header, final List<Pair<FundRank, Double>> values) {
        headers.add(header);
        for (int j = 0; j < values.size(); ++j) {
            final Pair<FundRank, Double> p = values.get(j);
            final FundRank fr = p.first();
            final int points = values.size() - j;
            fr.values.add(new Pair<Integer, Double>(points, p.second()));
        }
    }

    private List<Pair<FundRank, Double>> getFees() {
        final List<Pair<FundRank, Double>> res = new ArrayList<>();
        funds.forEach(fund -> {
            res.add(new Pair<>(fund, fund.getFee()));
        });
        Collections.sort(res, (a, b) -> Double.compare(b.second(), a.second()));
        return res;
    }

    private List<Pair<FundRank, Double>> getSharpeRatio() {
        final List<Pair<FundRank, Double>> res = new ArrayList<>();
        for (final FundRank fund : funds) {
            if (fund.avanza.sharpeRatio == null) {
                return null;
            }
            res.add(new Pair<>(fund, fund.avanza.sharpeRatio));
        }
        Collections.sort(res, (a, b) -> Double.compare(a.second(), b.second()));
        return res;
    }

    private List<Pair<FundRank, Double>> getValues(final int level) {
        final List<Pair<FundRank, Double>> res = new ArrayList<>();
        for (final FundRank fund : funds) {
            final Double value = getValue(fund.avanza, level);
            if (value == null) {
                return null;
            }
            res.add(new Pair<>(fund, value));
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
        public double mean;
        public final List<Pair<Integer, Double>> values = new ArrayList<>();
        private final Double fee;

        public FundRank(final AvanzaFund avanza, final Double fee) {
            this.avanza = avanza;
            this.fee = fee;
        }

        public double getFee() {
            return fee != null ? fee : avanza.productFee;
        }
    }

}
