package com.github.andreasarvidsson.founds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    public double getRegion(final String regionName) {
        if (regionsMap.containsKey(regionName)) {
            return regionsMap.get(regionName).y;
        }
        return 0.0;
    }

}
