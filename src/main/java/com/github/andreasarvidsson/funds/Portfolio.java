package com.github.andreasarvidsson.funds;

import com.github.andreasarvidsson.funds.util.Sum;
import com.github.andreasarvidsson.funds.util.Values;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public class Portfolio {

    public final List<FundData> funds = new ArrayList();
    public final Values companiesSize = new Values();
    public final Values countries = new Values();
    public final Values sectors = new Values();
    public final Values regions = new Values();
    public final Values holdings = new Values();
    public final Sum sum = new Sum();
    public final Sum developments = new Sum();
    public final String name;
    public double percentageSum, avgFee, risk;

    public Portfolio(
            final String name,
            final SelectedFund... selectedFunds) throws IOException {
        this(name, Arrays.asList(selectedFunds));
    }

    public Portfolio(
            final String name,
            final List<SelectedFund> selectedFunds) throws IOException {
        this.name = name;
        for (final SelectedFund sf : selectedFunds) {
            percentageSum += sf.percentage;
        }
        for (final SelectedFund sd : selectedFunds) {
            funds.add(new FundData(
                    sd.percentage,
                    sd.percentage / percentageSum,
                    Avanza.getFund(sd.name),
                    sd.fee
            ));
        }
        percentageSum *= 100;
        for (final FundData fd : funds) {
            avgFee += fd.getFee() * fd.percentageNormalized;
            risk += fd.avanza.risk * fd.percentageNormalized;
            if (fd.avanza.sharpeRatio != null) {
                sum.add(Headers.SHARPE_RATIO, fd.avanza.sharpeRatio, fd.percentageNormalized);
            }
            if (fd.avanza.standardDeviation != null) {
                sum.add(Headers.STANDARD_DEVIATION, fd.avanza.standardDeviation, fd.percentageNormalized);
            }
            if (fd.avanza.hasNonDevelopedMarkets()) {
                sum.add(Headers.NON_DEVELOPED_MARKETS, fd.avanza.getNonDevelopedMarkets(), fd.percentageNormalized);
            }
            Headers.DEVELOPMENT_TITLES.forEach(key -> {
                if (fd.avanza.hasDevelopment(key)) {
                    developments.add(key, fd.avanza.getDevelopment(key), fd.percentageNormalized);
                }
            });
            fd.avanza.countryChartData.forEach(data -> {
                countries.add(data.name, data.y * fd.percentageNormalized);
            });
            fd.avanza.regionChartData.forEach(data -> {
                regions.add(data.name, data.y * fd.percentageNormalized);
            });
            fd.avanza.sectorChartData.forEach(data -> {
                sectors.add(data.name, data.y * fd.percentageNormalized);
            });
            fd.avanza.holdingChartData.forEach(data -> {
                holdings.add(data.name, data.y * fd.percentageNormalized);
            });
//            if (fd.morningstar != null) {
//                companiesSize.add("Stora bolag", fd.morningstar.largeCompanies * fd.percentageNormalized);
//                companiesSize.add("Medelstora bolag", fd.morningstar.middleCompanies * fd.percentageNormalized);
//                companiesSize.add("Små bolag", fd.morningstar.smallCompanies * fd.percentageNormalized);
//                final double swePercentage = fd.avanza.getRegion(Regions.SWEDEN) / 100;
//                companiesSize.add("Stora svenska bolag", fd.morningstar.largeCompanies * fd.percentageNormalized * swePercentage);
//                companiesSize.add("Medelstora svenska bolag", fd.morningstar.middleCompanies * fd.percentageNormalized * swePercentage);
//                companiesSize.add("Små svenska bolag", fd.morningstar.smallCompanies * fd.percentageNormalized * swePercentage);
//            }
        }
//        if (!companiesSize.isEmpty() && regions.has(Region.SWEDEN.name)) {
//            final double sweSum = regions.get(Region.SWEDEN.name) * 0.01;
//            companiesSize.normalize("Stora svenska bolag", sweSum);
//            companiesSize.normalize("Medelstora svenska bolag", sweSum);
//            companiesSize.normalize("Små svenska bolag", sweSum);
//        }
        countries.compile(true);
        regions.compile(true);
        sectors.compile(true);
        holdings.compile(true);
        companiesSize.compile();
    }

}
