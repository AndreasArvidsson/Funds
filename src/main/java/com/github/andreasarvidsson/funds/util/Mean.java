package com.github.andreasarvidsson.funds.util;

/**
 *
 * @author Andreas Arvidsson
 */
public class Mean {

    private int count = 0;
    private double sum = 0;
    private double prod = 1;

    public void add(final int value) {
        sum += value;
        prod *= value;
        ++count;
    }

    public double geometric() {
        return Math.pow(prod, 1.0 / count);
    }

    public double aritmethic() {
        return sum / count;
    }

}
