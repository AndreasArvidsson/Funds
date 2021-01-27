package com.github.andreasarvidsson.funds;

/**
 *
 * @author Andreas Arvidsson
 */
public class SelectedFund {

    final String name;
    final Double percentage, fee;

    public SelectedFund(
            final double percentage,
            final String name,
            final double fee) {
        this.percentage = percentage / 100;
        this.name = name;
        this.fee = fee;
    }

    public SelectedFund(
            final String name) {
        this.percentage = null;
        this.name = name;
        this.fee = null;
    }

    public SelectedFund(
            final String name,
            final double fee) {
        this.percentage = null;
        this.name = name;
        this.fee = fee;
    }

    public SelectedFund(
            final double percentage,
            final String name) {
        this.percentage = percentage / 100;
        this.name = name;
        this.fee = null;
    }

}
