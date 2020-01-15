package com.github.andreasarvidsson.founds;

/**
 *
 * @author Andreas Arvidsson
 */
public class SelectedFound {

    final String name;
    final String[] alternativeNames;
    final double percentage;

    public SelectedFound(final double percentage, final String name, final String... alternativeNames) {
        this.name = name;
        this.alternativeNames = alternativeNames;
        this.percentage = percentage / 100;
    }

}