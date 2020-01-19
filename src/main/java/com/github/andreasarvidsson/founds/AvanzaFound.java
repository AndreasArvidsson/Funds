package com.github.andreasarvidsson.founds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class AvanzaFound {

    public String name;
    public double productFee;
    public Double developmentOneDay, developmentOneMonth, developmentThreeMonths,
            developmentSixMonths, developmentThisYear, developmentOneYear,
            developmentThreeYears, developmentFiveYears, sharpeRatio, standardDeviation;
    public int risk;
    public List<String> categories;
    public List<ChartData> countryChartData, holdingChartData,
            sectorChartData, regionChartData;
    public final Map<String, ChartData> regionsMap = new HashMap();
    private Map<String, Double> developmentMap;

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

    public double getRegion(final String regionName) {
        if (regionsMap.containsKey(regionName)) {
            return regionsMap.get(regionName).y;
        }
        return 0.0;
    }

    public boolean hasDevelopment(final String key) {
        if (developmentMap == null) {
            developmentMap = compileDevelopmentMap();
        }
        return developmentMap.containsKey(key);
    }

    public double getDevelopment(final String key) {
        if (developmentMap == null) {
            developmentMap = compileDevelopmentMap();
        }
        return developmentMap.get(key);
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
