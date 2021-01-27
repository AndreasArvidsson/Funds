package com.github.andreasarvidsson.funds;

/**
 *
 * @author Andreas Arvidsson
 */
public class FundData {

    public final AvanzaFund avanza;
    public final double percentage, percentageNormalized;
    private final Double fee;

    public FundData(
            final double percentage,
            final double percentageNormalized,
            final AvanzaFund avanza,
            final Double fee) {
        this.percentage = percentage;
        this.percentageNormalized = percentageNormalized;
        this.avanza = avanza;
        this.fee = fee;
    }

    public double getFee() {
        return fee != null ? fee : avanza.productFee;
    }

}
