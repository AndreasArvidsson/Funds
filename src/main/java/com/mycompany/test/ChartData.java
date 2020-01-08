package com.mycompany.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author Andreas Arvidsson
 */
@JsonInclude(Include.NON_NULL)
public class ChartData {

    public String name, type, currency, countryCode;
    public Double y;

}
