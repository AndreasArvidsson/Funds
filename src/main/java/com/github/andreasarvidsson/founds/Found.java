package com.github.andreasarvidsson.founds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andreas Arvidsson
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Found {

    public String name;
    public double productFee;
    public Double developmentOneDay, developmentOneMonth, developmentThreeMonths,
            developmentSixMonths, developmentThisYear, developmentOneYear,
            developmentThreeYears, developmentFiveYears;
    public List<String> categories;
    public List<ChartData> countryChartData, holdingChartData,
            sectorChartData, regionChartData;
    public final Map<String, ChartData> regionsMap = new HashMap();

    public void setCountryChartData(final List<ChartData> countryChartData) {
        this.countryChartData = countryChartData;
        countryChartData.forEach(chartData -> {
            final String regionName = Regions.get(chartData.name);
            if (!regionsMap.containsKey(regionName)) {
                final ChartData cd = new ChartData();
                cd.name = regionName;
                cd.y = 0.0;
                regionsMap.put(regionName, cd);
            }
            regionsMap.get(regionName).y += chartData.y;
        });
        regionChartData = new ArrayList(regionsMap.values());
        Collections.sort(regionChartData, (a, b) -> Double.compare(b.y, a.y));
    }

    public static final List<String> DEVELOPMENT_TITLES = Arrays.asList(
            "1 d", "1 m", "3 m", "6 m", "i år", "1 år", "3 år", "5 år"
    );

    public List<Pair<String, Double>> getDevelopment() {
        final List<Pair<String, Double>> res = new ArrayList();
        final Iterator<String> it = DEVELOPMENT_TITLES.iterator();
        if (developmentOneDay != null) {
            res.add(new Pair(it.next(), developmentOneDay));
        }
        if (developmentOneMonth != null) {
            res.add(new Pair(it.next(), developmentOneMonth));
        }
        if (developmentThreeMonths != null) {
            res.add(new Pair(it.next(), developmentThreeMonths));
        }
        if (developmentSixMonths != null) {
            res.add(new Pair(it.next(), developmentSixMonths));
        }
        if (developmentThisYear != null) {
            res.add(new Pair(it.next(), developmentThisYear));
        }
        if (developmentOneYear != null) {
            res.add(new Pair(it.next(), developmentOneYear));
        }
        if (developmentThreeYears != null) {
            res.add(new Pair(it.next(), developmentThreeYears));
        }
        if (developmentFiveYears != null) {
            res.add(new Pair(it.next(), developmentFiveYears));
        }
        return res;
    }

    public double getRegion(final String regionName) {
        if (regionsMap.containsKey(regionName)) {
            return regionsMap.get(regionName).y;
        }
        return 0.0;
    }

}
