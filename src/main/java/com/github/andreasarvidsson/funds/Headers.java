package com.github.andreasarvidsson.funds;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class Headers {

    public final static String SHARPE_RATIO = "Sharpekvot";
    public final static String STANDARD_DEVIATION = "Stdav (%)";
    public final static String NON_DEVELOPED_MARKETS = "EM/FM (%)";
    public final static String FEE = "Avgift (%)";
    public final static String RISK = "Risk";
    public final static String PERCENTAGE = "Andel (%)";
    public final static String DIFFERENCE = "Skillnad (%)";
    public final static String HOLDINGS = "Innehav";
    public final static String SECTOR = "Bransch";
    public final static String LAND = "Land";
    public final static String REGION = "Region";
    public static final String T_1_D = "1 d";
    public static final String T_1_M = "1 m";
    public static final String T_3_M = "3 m";
    public static final String T_6_M = "6 m";
    public static final String T_Y = "i år";
    public static final String T_1_Y = "1 år";
    public static final String T_3_Y = "3 år";
    public static final String T_5_Y = "5 år";

    public static final List<String> DEVELOPMENT_TITLES = Arrays.asList(
            T_1_D, T_1_M, T_3_M, T_6_M, T_Y, T_1_Y, T_3_Y, T_5_Y
    );

}
