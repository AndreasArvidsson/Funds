package com.github.andreasarvidsson.founds;

import com.github.andreasarvidsson.founds.util.Sum;
import com.github.andreasarvidsson.founds.util.Values;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public class Portfolio {

    public final List<FoundData> founds = new ArrayList();
    public final Values companiesSize = new Values();
    public final Values countries = new Values();
    public final Values sectors = new Values();
    public final Values regions = new Values();
    public final Sum sum = new Sum();
    public final Sum developments = new Sum();
    public final String name;
    public double percentageSum, avgFee, risk;

    public Portfolio(
            final String name,
            final SelectedFound... selectedFounds) throws IOException {
        this(name, Arrays.asList(selectedFounds));
    }

    public Portfolio(
            final String name,
            final List<SelectedFound> selectedFounds) throws IOException {
        this.name = name;
        for (final SelectedFound sf : selectedFounds) {
            percentageSum += sf.percentage;
        }
        for (final SelectedFound sd : selectedFounds) {
            founds.add(new FoundData(
                    sd.percentage,
                    sd.percentage / percentageSum,
                    Avanza.getFound(sd.name, sd.alternativeNames),
                    Morningstar.getFound(sd.name, sd.alternativeNames)
            ));
        }
        percentageSum *= 100;
        for (final FoundData fd : founds) {
            avgFee += fd.avanza.productFee * fd.percentageNormalized;
            risk += fd.avanza.risk * fd.percentageNormalized;
            if (fd.avanza.sharpeRatio != null) {
                sum.add(Headers.SHARP_RATIO, fd.avanza.sharpeRatio, fd.percentageNormalized);
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
        if (!companiesSize.isEmpty()) {
            final double sweSum = regions.get(Regions.SWEDEN) * 0.01;
            companiesSize.normalize("Stora svenska bolag", sweSum);
            companiesSize.normalize("Medelstora svenska bolag", sweSum);
            companiesSize.normalize("Små svenska bolag", sweSum);
        }
        countries.compile(true);
        regions.compile(true);
        sectors.compile(true);
        companiesSize.compile();
        sum.normalize();
        developments.normalize();
    }

}
