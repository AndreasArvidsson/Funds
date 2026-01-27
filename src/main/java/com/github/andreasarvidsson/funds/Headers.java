package com.github.andreasarvidsson.funds;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class Headers {

    public static final String SHARPE_RATIO = "Sharpekvot";
    public static final String STANDARD_DEVIATION = "Stdav (%)";
    public static final String NON_DEVELOPED_MARKETS = "EM/FM (%)";
    public static final String FEE = "Avgift (%)";
    public static final String RISK = "Risk";
    public static final String PERCENTAGE = "Andel (%)";
    public static final String DIFFERENCE = "Skillnad (%)";
    public static final String HOLDINGS = "Innehav";
    public static final String SECTOR = "Bransch";
    public static final String LAND = "Land";
    public static final String REGION = "Region";
    public static final String T_1_D = "1 d";
    public static final String T_1_M = "1 m";
    public static final String T_3_M = "3 m";
    public static final String T_6_M = "6 m";
    public static final String T_Y = "i år";
    public static final String T_1_Y = "1 år";
    public static final String T_3_Y = "3 år";
    public static final String T_5_Y = "5 år";

    public static final List<String> DEVELOPMENT_TITLES = Arrays.asList(
        T_1_D,
        T_1_M,
        T_3_M,
        T_6_M,
        T_Y,
        T_1_Y,
        T_3_Y,
        T_5_Y
    );
}
