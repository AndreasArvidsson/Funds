package com.github.andreasarvidsson.funds;

/**
 *
 * @author Andreas Arvidsson
 */
public class FundData {

    public final AvanzaFund avanza;
    public final MorningstarFund morningstar;
    public final SavrFund savr;
    public final double percentage, percentageNormalized;

    public FundData(
            final double percentage,
            final double percentageNormalized,
            final AvanzaFund avanza,
            final MorningstarFund morningstar,
            final SavrFund savr) {
        this.percentage = percentage;
        this.percentageNormalized = percentageNormalized;
        this.avanza = avanza;
        this.morningstar = morningstar;
        this.savr = savr;
    }

    public double getFee() {
        return savr != null ? savr.fee : avanza.productFee;
    }

}
