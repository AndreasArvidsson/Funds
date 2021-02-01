package com.github.andreasarvidsson.funds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.andreasarvidsson.funds.Country.Market;
import com.github.andreasarvidsson.funds.Country.Region;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andreas Arvidsson
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvanzaFund {

    public String name, isin;
    public double productFee;
    public Double developmentOneDay, developmentOneMonth, developmentThreeMonths,
            developmentSixMonths, developmentThisYear, developmentOneYear,
            developmentThreeYears, developmentFiveYears, sharpeRatio, standardDeviation;
    public int risk;
    public List<String> categories;
    public List<ChartData> countryChartData, holdingChartData,
            sectorChartData, regionChartData;
    public final Map<Country, ChartData> countryMap = new HashMap();
    public final Map<Region, ChartData> regionsMap = new HashMap();
    public final Map<Market, ChartData> marketMap = new HashMap();
    private Map<String, Double> developmentMap;

    public void compile() {
        countryChartData.forEach(chartData -> {
            final Country country = Country.fromString(chartData.name);
            countryMap.put(country, chartData);

            if (!regionsMap.containsKey(country.region)) {
                final ChartData cd = new ChartData();
                cd.name = country.region.toString();
                cd.y = 0.0;
                regionsMap.put(country.region, cd);
            }
            regionsMap.get(country.region).y += chartData.y;

            if (!marketMap.containsKey(country.market)) {
                final ChartData cd = new ChartData();
                cd.name = country.market.toString();
                cd.y = 0.0;
                marketMap.put(country.market, cd);
            }
            marketMap.get(country.market).y += chartData.y;
        });
        regionChartData = new ArrayList(regionsMap.values());
        Collections.sort(regionChartData, (a, b) -> Double.compare(b.y, a.y));
    }

    public Double getCountry(final Country country, final Double def) {
        if (countryMap.containsKey(country)) {
            return countryMap.get(country).y;
        }
        return def;
    }

    public Double getRegion(final Region region, final Double def) {
        if (regionsMap.containsKey(region)) {
            return regionsMap.get(region).y;
        }
        return def;
    }

    public boolean hasDevelopment(final String key) {
        if (developmentMap == null) {
            developmentMap = compileDevelopmentMap();
        }
        return developmentMap.containsKey(key);
    }

    public Double getDevelopment(final String key, final Double def) {
        if (developmentMap == null) {
            developmentMap = compileDevelopmentMap();
        }
        if (developmentMap.containsKey(key)) {
            return developmentMap.get(key);
        }
        return def;
    }

    public boolean hasNonDevelopedMarkets() {
        for (final Market m : marketMap.keySet()) {
            if (m != Market.DEVELOPED) {
                return true;
            }
        }
        return false;
    }

    public Double getNonDevelopedMarkets() {
        if (!hasNonDevelopedMarkets()) {
            return 0.0;
        }
        double res = 0;
        for (final Map.Entry<Market, ChartData> e : marketMap.entrySet()) {
            if (e.getKey() != Market.DEVELOPED) {
                res += e.getValue().y;
            }
        }
        return res;
    }

    private Map<String, Double> compileDevelopmentMap() {
        final Map<String, Double> res = new HashMap();
        Headers.DEVELOPMENT_TITLES.forEach(key -> {
            switch (key) {
                case Headers.T_1_D:
                    if (developmentOneDay != null) {
                        res.put(key, developmentOneDay);
                    }
                    break;
                case Headers.T_1_M:
                    if (developmentOneMonth != null) {
                        res.put(key, developmentOneMonth);
                    }
                    break;
                case Headers.T_3_M:
                    if (developmentThreeMonths != null) {
                        res.put(key, developmentThreeMonths);
                    }
                    break;
                case Headers.T_6_M:
                    if (developmentSixMonths != null) {
                        res.put(key, developmentSixMonths);
                    }
                    break;
                case Headers.T_Y:
                    if (developmentThisYear != null) {
                        res.put(key, developmentThisYear);
                    }
                    break;
                case Headers.T_1_Y:
                    if (developmentOneYear != null) {
                        res.put(key, developmentOneYear);
                    }
                    break;
                case Headers.T_3_Y:
                    if (developmentThreeYears != null) {
                        res.put(key, developmentThreeYears);
                    }
                    break;
                case Headers.T_5_Y:
                    if (developmentFiveYears != null) {
                        res.put(key, developmentFiveYears);
                    }
                    break;
            }
        });
        return res;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChartData {

        public String name, type, currency, countryCode;
        public Double y;

    }

}
