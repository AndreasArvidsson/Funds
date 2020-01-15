package com.github.andreasarvidsson.founds;

/**
 *
 * @author Andreas Arvidsson
 */
public class FoundData {

    public final AvanzaFound avanza;
    public final MorningstarFound morningstar;
    public final double percentage, percentageNormalized;

    public FoundData(
            final double percentage,
            final double percentageNormalized,
            final AvanzaFound avanza,
            final MorningstarFound morningstar) {
        this.percentage = percentage;
        this.percentageNormalized = percentageNormalized;
        this.avanza = avanza;
        this.morningstar = morningstar;
    }

    public FoundData(
            final double percentage,
            final double percentageNormalized,
            final AvanzaFound avanza) {
        this.percentage = percentage;
        this.percentageNormalized = percentageNormalized;
        this.avanza = avanza;
        this.morningstar = null;
    }

}
