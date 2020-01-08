package com.mycompany.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
    public List<ChartData> countryChartData, holdingChartData, sectorChartData;

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

}
