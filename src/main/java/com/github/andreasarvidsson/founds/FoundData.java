package com.github.andreasarvidsson.founds;

/**
 *
 * @author Andreas Arvidsson
 */
public class FoundData {

    public final AvanzaFound avanza;
    public final MorningstarFound morningstar;
    public final SavrFound savr;
    public final double percentage, percentageNormalized;

    public FoundData(
            final double percentage,
            final double percentageNormalized,
            final AvanzaFound avanza,
            final MorningstarFound morningstar,
            final SavrFound savr) {
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
